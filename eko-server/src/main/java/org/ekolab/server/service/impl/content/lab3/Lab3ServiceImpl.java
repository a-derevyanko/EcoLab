package org.ekolab.server.service.impl.content.lab3;

import com.twelvemonkeys.image.ImageUtil;
import net.sf.dynamicreports.report.constant.PageType;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.math3.util.Precision;
import org.ekolab.server.common.I18NUtils;
import org.ekolab.server.common.MathUtils;
import org.ekolab.server.dao.api.content.lab3.Lab3Dao;
import org.ekolab.server.model.content.lab3.City;
import org.ekolab.server.model.content.lab3.FuelType;
import org.ekolab.server.model.content.lab3.Lab3Data;
import org.ekolab.server.model.content.lab3.Lab3Variant;
import org.ekolab.server.model.content.lab3.NumberOfStacks;
import org.ekolab.server.model.content.lab3.NumberOfUnits;
import org.ekolab.server.model.content.lab3.UnitOutput;
import org.ekolab.server.model.content.lab3.WindDirection;
import org.ekolab.server.service.api.content.lab3.Lab3ChartType;
import org.ekolab.server.service.api.content.lab3.Lab3ResourceService;
import org.ekolab.server.service.api.content.lab3.Lab3Service;
import org.ekolab.server.service.impl.content.DataValue;
import org.ekolab.server.service.impl.content.LabServiceImpl;
import org.ekolab.server.service.impl.content.equations.ferrari.EquationFunction;
import org.ekolab.server.service.impl.content.equations.ferrari.QuarticFunction;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYDrawableAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.LineBorder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.CompositeTitle;
import org.jfree.chart.title.ImageTitle;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.title.Title;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static java.lang.Math.pow;

/**
 * Created by 777Al on 26.04.2017.
 */
@Service
public class Lab3ServiceImpl extends LabServiceImpl<Lab3Data, Lab3Variant, Lab3Dao> implements Lab3Service {
    private static final Logger LOGGER = LoggerFactory.getLogger(Lab3ServiceImpl.class);

    private static final int BORDER = 4000;
    private static final int BIG_SERIES_STEP = 100;
    private static final int SMALL_SERIES_STEP = 10;
    private static final Color EKO_LAB_COLOR = new Color(143, 184, 43);

    private final Lab3ResourceService lab3ResourceService;

    @Autowired
    public Lab3ServiceImpl(Lab3Dao lab3Dao, Lab3ResourceService lab3ResourceService) {
        super(lab3Dao);
        this.lab3ResourceService = lab3ResourceService;
    }

    @Override
    public Set<DataValue> getInitialDataValues(Lab3Variant data, Locale locale) {
        Set<DataValue> values = super.getInitialDataValues(data, locale);
        DataValue cityValue = new DataValue();
        cityValue.setName(messageSource.getMessage("lab3.initial-data.city", null, locale) + ": " + getFieldValueForPrint(data.getCity(), locale));
        cityValue.setValue(lab3ResourceService.getCoatOfArms(data.getCity()));
        values.add(cityValue);

        DataValue desulphurizationUnitType = new DataValue();
        desulphurizationUnitType.setName(messageSource.getMessage("desulphurizationUnitType", null, locale));
        desulphurizationUnitType.setValue(messageSource.getMessage("desulphurizationUnitType-none", null, locale));
        values.add(desulphurizationUnitType);
        return values;
    }

    @Override
    protected Map<String, Object> getInitialDataWithLocalizedValues(Lab3Variant data, Locale locale) {
        Map<String, Object> map = super.getInitialDataWithLocalizedValues(data, locale);
        map.put("windDirection", lab3ResourceService.getWindRose(data.getCity()));
        map.remove("city");

        return map;
    }

    @Override
    protected Map<String, Object> getValuesForReport(Lab3Data labData, Locale locale) {
        Map<String, Object> values = new HashMap<>(super.getValuesForReport(labData, locale));

        Image isoLineImage = ImageUtil.createRotated(createChart(labData, locale, Lab3ChartType.ISOLINE).
                createBufferedImage(PageType.A4.getHeight(), 480), ImageUtil.ROTATE_90_CCW);

        Image so2LineImage = ImageUtil.createRotated(createChart(labData, locale, Lab3ChartType.SO2).
                createBufferedImage(PageType.A4.getHeight(), 480), ImageUtil.ROTATE_90_CCW);

        Image ashLineImage = ImageUtil.createRotated(createChart(labData, locale, Lab3ChartType.ASH).
                createBufferedImage(PageType.A4.getHeight(), 480), ImageUtil.ROTATE_90_CCW);
        values.put("mapImageIsoline", isoLineImage);
        values.put("mapImageSo2", so2LineImage);
        values.put("mapImageAsh", ashLineImage);
        return values;
    }

    @Override
    public void updateCalculatedFields(Lab3Data labData) {
        if (labData.getCombustionProductsVolume() != null && labData.getFuelConsumer() != null
                && labData.getStacksDiameter() != null) {
            labData.setStackAverageGasesSpeed((4 * labData.getCombustionProductsVolume() * labData.getFuelConsumer() * labData.getNumberOfUnits().value()) /
                    (3.6 * Math.PI * Math.pow(labData.getStacksDiameter(), 2) * labData.getNumberOfStacks().value()));
        }

        if (labData.getCombustionProductsVolume() != null && labData.getWaterVaporVolume() != null &&
                labData.getExcessAirRatio() != null && labData.getAirVolume() != null &&
                labData.getCarbonInFlyAsh() != null && labData.getFuelConsumer() != null &&
                labData.getNumberOfUnits() != null) {

            double V = labData.getCombustionProductsVolume() - labData.getWaterVaporVolume() +
                    (labData.getExcessAirRatio() - 1) * labData.getAirVolume();
            double B = (1 - labData.getCarbonInFlyAsh() / 100) * labData.getFuelConsumer();

            labData.setNoxMassiveInjection(labData.getFlueGasNOxConcentration() * labData.getNumberOfUnits().value() * V * B * 0.000278);
            labData.setNo2MassiveInjection(0.8 * labData.getNoxMassiveInjection());

            labData.setNoMassiveInjection(0.13 * labData.getNoxMassiveInjection());
        }

        if (labData.getFuelConsumer() != null && labData.getSulphurContent() != null && labData.getSulphurOxidesFractionAssociatedByFlyAsh() != null
                && labData.getSulphurOxidesFractionAssociatedInWetDustCollector() != null
                && labData.getSulphurOxidesFractionAssociatedInDesulphurizationSystem() != null
                && labData.getDesulphurizationSystemRunningTime() != null
                && labData.getBoilerRunningTime() != null) {
            labData.setSo2MassiveInjection(0.02 * (1000 / 3.6) * labData.getNumberOfUnits().value() *
                    labData.getFuelConsumer() * labData.getSulphurContent() *
                    (1 - labData.getSulphurOxidesFractionAssociatedByFlyAsh()) *
                    (1 - labData.getSulphurOxidesFractionAssociatedInWetDustCollector()) *
                    (1 - (labData.getSulphurOxidesFractionAssociatedInDesulphurizationSystem()  / 100) *
                            labData.getDesulphurizationSystemRunningTime() / labData.getBoilerRunningTime()));
        }

        if (labData.getFuelConsumer() != null && labData.getAshProportionEntrainedGases() != null
                && labData.getAshContent() != null
                && labData.getCarbonInFlyAsh() != null
                && labData.getLowHeatValue() != null
                && labData.getAshRecyclingFactor() != null) {
            labData.setAshMassiveInjection(0.01 * (1000 / 3.6) * labData.getNumberOfUnits().value() * labData.getFuelConsumer() * (labData.getAshProportionEntrainedGases() *
                    labData.getAshContent() +
                    labData.getCarbonInFlyAsh() *
                            labData.getLowHeatValue() / 32.68) * (1 - labData.getAshRecyclingFactor() / 100));
        }

        if (labData.getStackExitTemperature() != null && labData.getOutsideAirTemperature() != null &&
                labData.getFuelConsumer() != null &&
                labData.getCombustionProductsVolume() != null &&
                labData.getStackAverageGasesSpeed() != null &&
                labData.getStacksDiameter() != null &&
                labData.getStacksHeight() != null) {
            double dT = labData.getStackExitTemperature() - labData.getOutsideAirTemperature();
            double f = 1000 * (Math.pow(labData.getStackAverageGasesSpeed(), 2) * labData.getStacksDiameter()) /
                    (Math.pow(labData.getStacksHeight(), 2) * dT);

            double V1 = labData.getCombustionProductsVolume() * labData.getFuelConsumer() / 3.6;
            double vm = 0.65 * Math.cbrt(V1 * labData.getNumberOfUnits().value() * dT / (labData.getStacksHeight() * labData.getNumberOfStacks().value()));
            double vM = 1.3 * labData.getStackAverageGasesSpeed() * labData.getStacksDiameter() / labData.getStacksHeight();

            labData.setVM(vm);

            double fe = 800 * Math.pow(vM, 3);

            if (f < 100 && fe < f) {
                f = fe;
            }

            labData.setF(f);

            double m = f < 100 ?
                    1.0 / (0.67 + 0.1 * Math.sqrt(f) + 0.34 * Math.cbrt(f)) :
                    1.47 / Math.cbrt(f);

            labData.setM(m);
            double n;
            double d;
            if (f >= 100 || dT < 0.1) {
                labData.setBreakdownWindSpeed(vM <= 0.5 ?
                        0.5 :
                        vM <= 2 ?
                                vM :
                                vM * 2.2);

                n = vM < 0.5 ?
                        4.4 * vM :
                        vM < 2 ?
                                0.532 * Math.pow(vM, 2) - 2.13 * vM + 3.13 : 1;

                d = vM < 0.5 ?
                        5.7 :
                        vM < 2 ?
                                11.4 * vM : 16 * Math.sqrt(vM);
            } else {
                labData.setBreakdownWindSpeed(vm <= 0.5 ?
                        0.5 :
                        vm <= 2 ?
                                vm :
                                vm * (1 + 0.12 * Math.sqrt(f)));

                n = vm < 0.5 ?
                        4.4 * vm :
                        vm < 2 ?
                                0.532 * Math.pow(vm, 2) - 2.13 * vm + 3.13 : 1;

                d = vm < 0.5 ?
                        2.48 * (1 + 0.28 * Math.cbrt(fe)) :
                        vm < 2 ?
                                4.95 * vm * (1 + 0.28 * Math.cbrt(f)) :
                                7 * Math.sqrt(vm) * (1 + 0.28 * Math.cbrt(f));
            }

            labData.setN(n);
            labData.setD(d);

            if (labData.getTemperatureCoefficient() != null &&
                    labData.getHarmfulSubstancesDepositionCoefficient() != null &&
                    labData.getTerrainCoefficient() != null &&
                    labData.getNumberOfStacks() != null && labData.getWindSpeed() != null) {

                double uUm = labData.getWindSpeed() / labData.getBreakdownWindSpeed();
                double r = uUm > 1 ?
                        3 * uUm / (2 * Math.pow(uUm, 2) - uUm + 2) :
                        0.67 * uUm + 1.67 * Math.pow(uUm, 2) - 1.34 * Math.pow(uUm, 3);

                double k;
                if (f >= 100 || dT < 0.1) {
                    k = labData.getTemperatureCoefficient() * labData.getHarmfulSubstancesDepositionCoefficient() *
                            n * labData.getTerrainCoefficient()
                            / (Math.pow(labData.getStacksHeight(), 4.0 / 3.0) * 7.1 * Math.sqrt(labData.getStackAverageGasesSpeed() * V1 * labData.getNumberOfUnits().value() / labData.getNumberOfStacks().value()));
                } else {
                    k = labData.getTemperatureCoefficient() * labData.getHarmfulSubstancesDepositionCoefficient() *
                            n * m * labData.getTerrainCoefficient()
                            / (Math.pow(labData.getStacksHeight(), 2)) * Math.cbrt(labData.getNumberOfStacks().value() / (V1 * labData.getNumberOfUnits().value() * dT));
                }
                if (labData.getNoxMassiveInjection() != null) {
                    labData.setBwdNoxGroundLevelConcentration(labData.getNo2BackgroundConcentration() + k *
                            labData.getNoxMassiveInjection());
                }
                if (labData.getNo2MassiveInjection() != null) {
                    labData.setBwdNo2GroundLevelConcentration(labData.getNo2BackgroundConcentration() + k *
                            labData.getNo2MassiveInjection());

                    labData.setWindSpeedMaxNo2GroundLevelConcentration(labData.getBwdNo2GroundLevelConcentration() * r);
                }

                if (labData.getNoMassiveInjection() != null) {
                    labData.setBwdNoGroundLevelConcentration(labData.getNoBackgroundConcentration() + k *
                            labData.getNoMassiveInjection());

                    labData.setWindSpeedMaxNoGroundLevelConcentration(labData.getBwdNoGroundLevelConcentration() * r);
                }

                if (labData.getSo2MassiveInjection() != null) {
                    labData.setBwdSo2GroundLevelConcentration(labData.getSo2BackgroundConcentration() + k *
                            labData.getSo2MassiveInjection());

                    labData.setWindSpeedMaxSo2GroundLevelConcentration(labData.getBwdSo2GroundLevelConcentration() * r);
                }

                if (labData.getAshMassiveInjection() != null) {
                    labData.setBwdAshGroundLevelConcentration(labData.getAshBackgroundConcentration() + k *
                            labData.getAshMassiveInjection());

                    labData.setWindSpeedMaxAshGroundLevelConcentration(labData.getBwdAshGroundLevelConcentration() * r);
                }

                if (labData.getBwdNoGroundLevelConcentration() != null && labData.getBwdSo2GroundLevelConcentration() != null) {
                    labData.setNoAndSo2SummationGroup(labData.getBwdNoGroundLevelConcentration() / 0.4 +
                            labData.getBwdSo2GroundLevelConcentration() / 0.5);
                }
                // todo "Если q больше 1.6 то должно выскакивать уведомление «Концентрация веществ обладающих суммацией вредного действия превышена»"

                if (labData.getBwdNo2GroundLevelConcentration() != null && labData.getBwdSo2GroundLevelConcentration() != null) {
                    labData.setNo2AndSo2SummationGroup(labData.getBwdNo2GroundLevelConcentration() / 0.085 +
                            labData.getBwdSo2GroundLevelConcentration() / 0.5);
                }

            }

            if (labData.getStacksHeight() != null && labData.getHarmfulSubstancesDepositionCoefficient() != null) {
                labData.setBwdMaxGroundLevelConcentrationDistance(d * labData.getStacksHeight() *
                        (5.0 - labData.getHarmfulSubstancesDepositionCoefficient()) / 4.0);

                if (labData.getWindSpeed() != null && labData.getBreakdownWindSpeed() != null) {
                    double uUm = labData.getWindSpeed() / labData.getBreakdownWindSpeed();
                    double p = uUm <= 0.25 ? 3 :
                            uUm <= 1 ? 8.43 * Math.pow((1 - uUm), 3) + 1:
                                    0.32 * uUm + 0.68;
                    labData.setWindSpeedMaxGroundLevelConcentrationDistance(p * labData.getBwdMaxGroundLevelConcentrationDistance());
                }
            }
        }
    }

    @Override
    public Lab3Data createNewLabData() {
        return new Lab3Data();
    }

    @Override
    public JFreeChart createChart(Lab3Data labData, Locale locale, Lab3ChartType chartType) {
        Double bwdMaxGroundLevelConcentrationDistance = labData.getBwdMaxGroundLevelConcentrationDistance();
        Double harmfulSubstancesDepositionCoefficient = labData.getHarmfulSubstancesDepositionCoefficient();
        Double windSpeed = labData.getWindSpeed();
        if (bwdMaxGroundLevelConcentrationDistance != null && harmfulSubstancesDepositionCoefficient != null && windSpeed != null)
        {
            Double groundLevelConcentration;
            Double backgroundConcentration;
            Double mac;
            String chartTitle;
            if (chartType == Lab3ChartType.ISOLINE) {
                groundLevelConcentration = labData.getBwdNoxGroundLevelConcentration();
                backgroundConcentration = labData.getNo2BackgroundConcentration();
                mac = labData.getNo2MAC();
                chartTitle = messageSource.getMessage("lab3.isoline-chart-title-nox", null, locale);
            } else if (chartType == Lab3ChartType.SO2) {
                groundLevelConcentration = labData.getBwdSo2GroundLevelConcentration();
                backgroundConcentration = labData.getSo2BackgroundConcentration();
                mac = labData.getSo2MAC();
                chartTitle = messageSource.getMessage("lab3.isoline-chart-title-so2", null, locale);
            } else if (chartType == Lab3ChartType.ASH) {
                groundLevelConcentration = labData.getBwdAshGroundLevelConcentration();
                backgroundConcentration = labData.getAshBackgroundConcentration();
                mac = labData.getAshMAC();
                chartTitle = messageSource.getMessage("lab3.isoline-chart-title-ash", null, locale);
            } else {
                throw new IllegalArgumentException("Unknown chart type");
            }

            //todo пока проверки прибиты - нужно вынести их в валидацию полей
            if (mac > 1) {
                throw new IllegalArgumentException("Invalid data");
            }
            return createSplineChart(labData, chartTitle, createDataset(bwdMaxGroundLevelConcentrationDistance,
                    harmfulSubstancesDepositionCoefficient, groundLevelConcentration,
                    backgroundConcentration, windSpeed, mac, locale),
                    bwdMaxGroundLevelConcentrationDistance, groundLevelConcentration, locale);
        }
        throw new IllegalArgumentException("Invalid data");
    }

    @Override
    public int getLabNumber() {
        return 3;
    }

    @Override
    protected Lab3Variant generateNewLabVariant() {
        Lab3Variant variant = new Lab3Variant();

        WindDirection randomDirection = WindDirection.values()[RandomUtils.nextInt(WindDirection.values().length)];
        variant.setWindDirection(randomDirection);

        //Получим случайный город, скорость ветра в нем
        City randomCity = City.values()[RandomUtils.nextInt(City.values().length)];
        variant.setCity(randomCity);
        variant.setWindSpeed(randomCity.getWindSpeed());

        //Получим список типов топлива для этого города
        List<FuelType> fuelList = randomCity.getFuelTypesForTheCity();

        //Получим случайный тип топлива из списка, низжую теплоту сгорания топлива для него
        FuelType randomFuelType = fuelList.get(RandomUtils.nextInt(fuelList.size()));
        variant.setFuelType(randomFuelType);
        variant.setLowHeatValue(randomFuelType.getLowHeatValue());

        //Получим мощность 1 блока
        UnitOutput randomUnitOutput = UnitOutput.values()[RandomUtils.nextInt(UnitOutput.values().length)];

        //Получим количество блоков, паропроизводительность и общую мощность
        boolean oil = randomFuelType == FuelType.STABILIZED_OIL || randomFuelType == FuelType.SULFUR_OIL;
        List<NumberOfUnits> unitCounts = randomUnitOutput.getNumberOfUnits();
        List<Integer> stacksHeights = randomUnitOutput.getStacksHeights();
        NumberOfUnits randomUnitCount = unitCounts.get(RandomUtils.nextInt(unitCounts.size()));
        variant.setNumberOfUnits(randomUnitCount);
        variant.setSteamProductionCapacity(randomUnitOutput.getSteamProductionCapacity());
        List<NumberOfStacks> stacksCounts = randomUnitCount.getStacksCounts();
        NumberOfStacks randomStacksCount = stacksCounts.get(RandomUtils.nextInt(stacksCounts.size()));
        variant.setNumberOfStacks(randomStacksCount);
        Integer randomStacksHeight = stacksHeights.get(RandomUtils.nextInt(stacksHeights.size()));
        variant.setStacksHeight(randomStacksHeight);
        variant.setTppOutput(randomUnitCount.value() * randomUnitOutput.getUnitOutput());
        variant.setFuelConsumer(randomUnitOutput.getUnitOutput() * 29.3 * randomUnitOutput.getBy(oil) /
                (randomFuelType.getLowHeatValue() * 1000));
        variant.setSulphurContent(randomFuelType.getSulphurContent());
        variant.setAshContent(randomFuelType.getAshContent());
        variant.setWaterContent(randomFuelType.getWaterContent());
        variant.setAshRecyclingFactor(oil ? 0.0 : 92.0 + RandomUtils.nextInt(15) * 0.5);
        variant.setFlueGasNOxConcentration(randomFuelType.getFlueGasNOxConcentration());
        variant.setStackExitTemperature(randomFuelType.getStackExitTemperature());
        variant.setOutsideAirTemperature(randomCity.getOutsideAirTemperature());
        variant.setExcessAirRatio(1.4);
        variant.setAirVolume(0.0889 * (randomFuelType.getCarbonContent() + 0.375 * randomFuelType.getSulphurContent()) +
                0.265 * randomFuelType.getHydrogenContent() - 0.0333 * randomFuelType.getOxygenContent());
        variant.setWaterVaporVolume(0.111 * randomFuelType.getHydrogenContent() +
                0.0124 * randomFuelType.getWaterContent() + 0.016 * variant.getAirVolume());
        variant.setCombustionProductsVolume((1.866 * (randomFuelType.getCarbonContent() +
                0.375 * randomFuelType.getSulphurContent()) / 100) + (0.79 * variant.getAirVolume() + 0.8 *
                randomFuelType.getNitrogenContent() / 100) + variant.getWaterVaporVolume());
        variant.setNo2BackgroundConcentration(0.005 + RandomUtils.nextInt(11) * 0.001);
        variant.setNoBackgroundConcentration(0.008 + RandomUtils.nextInt(43) * 0.001);
        variant.setSo2BackgroundConcentration(0.005 + RandomUtils.nextInt(6) * 0.001);
        variant.setAshBackgroundConcentration(0.005 + RandomUtils.nextInt(6) * 0.001);
        return variant;
    }

    private XYSeriesCollection createDataset(double bwdMaxGroundLevelConcentrationDistance,
                                             double harmfulSubstancesDepositionCoefficient, double groundLevelConcentration,
                                             double backgroundConcentration, double windSpeed, double mac, Locale locale) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        int Xm = Math.toIntExact(Math.round(bwdMaxGroundLevelConcentrationDistance));

        for (double CyCoefficient = 0.1; CyCoefficient < 0.95; CyCoefficient += 0.1) {
            double C = CyCoefficient * groundLevelConcentration;
            if (C > backgroundConcentration) {
                String description = String.valueOf(Precision.round(CyCoefficient, 1));
                XYSeries series = new XYSeries("C = " + description +" Cm", false);
                series.setDescription(description);
                dataset.addSeries(series);
                fillIsoLineSeries(series, CyCoefficient, Xm, bwdMaxGroundLevelConcentrationDistance, harmfulSubstancesDepositionCoefficient, groundLevelConcentration,
                        windSpeed, Integer.MAX_VALUE);
            }
        }

        // Найдём максимальный X, для которого будем производить расчёт
        int maxX = findMaxX(dataset) + BORDER;

        String backgroundName = messageSource.getMessage("lab3.isoline-background-name", new Object[]{backgroundConcentration}, locale);
        XYSeries borderSeries = new XYSeries(backgroundName, false);
        borderSeries.setDescription(messageSource.getMessage("lab3.isoline-background-name-description", null, locale));
        dataset.addSeries(borderSeries);
        double borderCyCoefficient = backgroundConcentration / groundLevelConcentration;
        fillIsoLineSeries(borderSeries, borderCyCoefficient, Xm, bwdMaxGroundLevelConcentrationDistance, harmfulSubstancesDepositionCoefficient, groundLevelConcentration,
                windSpeed, maxX);

        if (groundLevelConcentration > mac) {
            XYSeries macSeries = new XYSeries(messageSource.getMessage("lab3.isoline-mac-name", new Object[]{mac}, locale), false);
            macSeries.setDescription("MAC");
            dataset.addSeries(macSeries);
            double macCyCoefficient = mac / groundLevelConcentration;
            fillIsoLineSeries(macSeries, macCyCoefficient, Xm, bwdMaxGroundLevelConcentrationDistance, harmfulSubstancesDepositionCoefficient, groundLevelConcentration,
                    windSpeed, maxX);
        }

        return dataset;
    }

    private int findMaxX(XYSeriesCollection dataset) {
        int maxX = 0;

        for (XYSeries series : (List<XYSeries>) dataset.getSeries()) {
            XYDataItem item = series.getDataItem(0);
            if (item.getX().intValue() > maxX) {
                maxX = item.getX().intValue();
            }
        }
        return maxX;
    }

    private void fillIsoLineSeries(XYSeries series, double CyCoefficient, int Xm, double windSpeedMaxGroundLevelConcentrationDistance,
                                   double harmfulSubstancesDepositionCoefficient, double groundLevelConcentration, double windSpeed, int maxBorderValue) {
        double C = CyCoefficient * groundLevelConcentration;

        // Находим граничные точки
        int x1 = countX0(0, Xm, windSpeedMaxGroundLevelConcentrationDistance, harmfulSubstancesDepositionCoefficient, CyCoefficient);
        int xN = countX0(Xm, Integer.MAX_VALUE, windSpeedMaxGroundLevelConcentrationDistance, harmfulSubstancesDepositionCoefficient, CyCoefficient);

        if (xN < maxBorderValue) {
            series.add(xN, 0);
        } else {
            xN = maxBorderValue;
            if (!"MAC".equals(series.getDescription())) {
                series.setDescription(null);
            }
        }
        Map<Integer, Double> points = new TreeMap<>();
        int step = xN - x1 < 2000 ? SMALL_SERIES_STEP : BIG_SERIES_STEP;
        for (int x = xN - 1; x > x1; x-=step) {
            double C1 = countS1(x, windSpeedMaxGroundLevelConcentrationDistance, harmfulSubstancesDepositionCoefficient) * groundLevelConcentration;
            EquationFunction f = new QuarticFunction(45.1, 17, 12.8, 5, 1 - Math.sqrt(C1 / C));
            for (double t : f.findRealRoots()) {
                if (t > 0) {
                    double y = Math.sqrt(windSpeed > 5 ? t * x * x / 5 : t * x * x / windSpeed);
                    points.put(x, y);
                    series.add(x, y);
                }
            }
        }

        series.add(x1, 0);

        for (Map.Entry<Integer, Double> point : points.entrySet()) {
            series.add((double) point.getKey(), -point.getValue());
        }
        if (series.getDescription() != null) {
            series.add(xN, 0);
        }
    }

    private int countX0(int fromDistance, int toDistance, double Xm,
                        double harmfulSubstancesDepositionCoefficient, double CyCoefficient) {
        for (int x = fromDistance; x < toDistance; x++) {
            double s1 = countS1(x, Xm, harmfulSubstancesDepositionCoefficient);
            if (MathUtils.checkEquals(s1, CyCoefficient)) {
                return x;
            }
        }
        throw new IllegalArgumentException(String.format("fromDistance = %s, toDistance = %s," +
                        " windSpeedMaxGroundLevelConcentrationDistance = %s, " +
                        "harmfulSubstancesDepositionCoefficient = %s, CyCoefficient = %s",
                fromDistance, toDistance, Xm,
                harmfulSubstancesDepositionCoefficient, CyCoefficient));
    }

    private double countS1(double x, double windSpeedMaxGroundLevelConcentrationDistance,
                           double harmfulSubstancesDepositionCoefficient) {
        double d = x / windSpeedMaxGroundLevelConcentrationDistance;
        if (d <= 1) {
            return 3 * pow(d, 4) - 8 * pow(d, 3) + 6 * pow(d, 2);
        } else if (d <= 8) {
            return 1.13 / (0.13 * pow(d, 2) + 1);
        } else {
            if (harmfulSubstancesDepositionCoefficient > 1.5) {
                return 1 / (0.1 * pow(d, 2) + 2.47 * d - 17.8);
            } else {
                return d / (3.58 * pow(d, 2) - 35.2 * d + 120);
            }
        }
    }

    private JFreeChart createSplineChart(Lab3Data labData,
                                         String chartName,
                                         XYSeriesCollection dataSet,
                                         double Xm,
                                         double Cm,
                                         Locale locale) {
        NumberAxis xAxis = new NumberAxis(messageSource.getMessage("lab3.isoline-x-axis", null, locale));
        NumberAxis yAxis = new NumberAxis(messageSource.getMessage("lab3.isoline-y-axis", null, locale));
        XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
        XYPlot plot = new XYPlot(dataSet, xAxis, yAxis, renderer);

        // Добавляем маркер с Xm
        final CircleDrawer cd = new CircleDrawer(Color.white, new BasicStroke(1.0f), Color.BLACK);
        final XYAnnotation CmPoint = new XYDrawableAnnotation(Xm, 0.0, 5.0, 5.0, cd);
        plot.addAnnotation(CmPoint);
        final XYTextAnnotation xMMarker = new XYTextAnnotation("Cm", Xm - 70.0, 0.0);
        xMMarker.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        xMMarker.setPaint(Color.BLUE);
        xMMarker.setTextAnchor(TextAnchor.HALF_ASCENT_RIGHT);
        plot.addAnnotation(xMMarker);

        List<XYSeries> seriesWithLabels = new ArrayList<>(dataSet.getSeries());

        boolean bigSeriesExists = seriesWithLabels.removeIf(xySeries -> xySeries.getDescription() == null);
        boolean macSeriesExists = seriesWithLabels.removeIf(xySeries -> "MAC".equals(xySeries.getDescription()));

        for (XYSeries series : seriesWithLabels) {
            final XYTextAnnotation seriesNameMarker = new XYTextAnnotation(series.getDescription(), series.getX(0).doubleValue(), 0.0);
            seriesNameMarker.setBackgroundPaint(Color.WHITE);
            seriesNameMarker.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 2));
            seriesNameMarker.setPaint(Color.BLUE);
            seriesNameMarker.setTextAnchor(TextAnchor.HALF_ASCENT_CENTER);
            plot.addAnnotation(seriesNameMarker);
        }
        plot.setBackgroundImage(lab3ResourceService.getBackgroundImage(labData.getCity(), labData.getWindDirection()));
        plot.setAxisOffset(new RectangleInsets(4, 4, 4, 4));

        JFreeChart chart = new JFreeChart(chartName,
                JFreeChart.DEFAULT_TITLE_FONT, plot, false);
        ImageTitle windRoseTitle = new ImageTitle(lab3ResourceService.getWindRoseImage(labData.getCity()), 150, 150,
                Title.DEFAULT_POSITION, Title.DEFAULT_HORIZONTAL_ALIGNMENT,
                Title.DEFAULT_VERTICAL_ALIGNMENT, Title.DEFAULT_PADDING);
        windRoseTitle.setPosition(RectangleEdge.LEFT);
        windRoseTitle.setPadding(10.0, 0.0, 20.0, 0.0);

        LegendTitle legendTitle = new LegendTitle(plot);
        legendTitle.setPosition(RectangleEdge.LEFT);

        ChartUtilities.applyCurrentTheme(chart);

        TextTitle textTitle = new TextTitle(messageSource.getMessage("lab3.isoline-text-legend",
                new Object[]{
                        messageSource.getMessage(I18NUtils.getEnumName(labData.getCity()), null, locale),
                        labData.getTppOutput(),
                        messageSource.getMessage(I18NUtils.getEnumName(labData.getVariant().getFuelType()), null, locale),
                        Precision.round(Cm, 1),
                        Precision.round(Xm, 1)}, locale), new Font(Font.SANS_SERIF, Font.BOLD, 12));
        textTitle.setTextAlignment(HorizontalAlignment.LEFT);

        BlockContainer blockContainer = new BlockContainer();
        blockContainer.add(textTitle, RectangleEdge.TOP);
        blockContainer.add(windRoseTitle);
        blockContainer.add(legendTitle, RectangleEdge.BOTTOM);
        blockContainer.setPadding(5.0, 5.0, 5.0, 5.0);
        CompositeTitle title = new CompositeTitle(blockContainer);
        title.setPosition(RectangleEdge.LEFT);
        title.setMargin(new RectangleInsets(1.0, 1.0, 1.0, 1.0));
        title.setFrame(new LineBorder());
        chart.addSubtitle(title);

        // Все линии, кроме граничной и ПДК делаем серого цвета
        for (int i = 0; i < seriesWithLabels.size(); i++) {
            renderer.setSeriesPaint(i, Color.BLACK);
        }

        if (macSeriesExists) {
            if (bigSeriesExists) {
                xAxis.setUpperBound(dataSet.getSeries(dataSet.getSeriesCount() - 2).getDataItem(0).getX().doubleValue());
            }
            renderer.setSeriesPaint(dataSet.getSeriesCount() - 2, EKO_LAB_COLOR);
            renderer.setSeriesStroke(dataSet.getSeriesCount() - 2, new BasicStroke(2.0f));
            dataSet.getSeries(dataSet.getSeriesCount() - 1).setDescription(null);
            renderer.setSeriesStroke(dataSet.getSeriesCount() - 1, new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{2.0f}, 0));
        } else {
            if (bigSeriesExists) {
                xAxis.setUpperBound(dataSet.getSeries(dataSet.getSeriesCount() - 1).getDataItem(0).getX().doubleValue());
            }
            renderer.setSeriesPaint(dataSet.getSeriesCount() - 1, EKO_LAB_COLOR);
            renderer.setSeriesStroke(dataSet.getSeriesCount() - 1, new BasicStroke(2.0f));
        }

        return chart;
    }
}
