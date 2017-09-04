package org.ekolab.server.service.impl.content.lab3;

import com.twelvemonkeys.image.ImageUtil;
import net.sf.dynamicreports.report.constant.ComponentPositionType;
import net.sf.dynamicreports.report.constant.ImageScale;
import net.sf.dynamicreports.report.constant.PageType;
import org.apache.commons.lang.math.RandomUtils;
import org.ekolab.server.dao.api.content.lab3.Lab3Dao;
import org.ekolab.server.model.content.lab3.*;
import org.ekolab.server.service.api.content.LabChartType;
import org.ekolab.server.service.api.content.lab3.IsoLineChartService;
import org.ekolab.server.service.api.content.lab3.Lab3ChartType;
import org.ekolab.server.service.api.content.lab3.Lab3Service;
import org.ekolab.server.service.impl.ReportTemplates;
import org.ekolab.server.service.impl.content.LabServiceImpl;
import org.jfree.chart.JFreeChart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;

/**
 * Created by 777Al on 26.04.2017.
 */
@Service
public class Lab3ServiceImpl extends LabServiceImpl<Lab3Data, Lab3Variant> implements Lab3Service {
    @Autowired
    private IsoLineChartService isoLineChartService;

    @Autowired
    public Lab3ServiceImpl(Lab3Dao lab3Dao) {
        super(lab3Dao);
    }

    /**
     * Возвращает печатный вариант отчёта в PDF формате.
     * На второй странице отчёта печатается график изолиний в вертикальной ориентации.
     *
     * @param labData данные лабораторной работы.
     * @param locale  язык.
     * @return печатный вариант данных в PDF формате.
     */
    @Override
    public byte[] createReport(Lab3Data labData, Locale locale) {
        return ReportTemplates.printReport(super.createBaseLabReport(labData, locale)
                .summary(cmp.image(ImageUtil.createRotated(createChart(labData, locale, Lab3ChartType.ISOLINE).
                        createBufferedImage(PageType.A4.getWidth(), PageType.A4.getHeight()), ImageUtil.ROTATE_90_CW))
                        .setMinDimension(Math.round(PageType.A4.getWidth() * 0.95f), Math.round(PageType.A4.getHeight() * 0.95f))
                        .setImageScale(ImageScale.FILL_FRAME).setPositionType(ComponentPositionType.FIX_RELATIVE_TO_BOTTOM)
                        .setStyle(ReportTemplates.ROTATED_CENTERED_STYLE)));
    }

    @Override
    public Lab3Data updateCalculatedFields(Lab3Data labData) {
        if (labData.getCombustionProductsVolume() != null && labData.getFuelConsumer() != null
                && labData.getStacksDiameter() != null) {
            labData.setStackAverageGasesSpeed((4 * labData.getCombustionProductsVolume() * labData.getFuelConsumer()) /
                    (3.6 * Math.PI * Math.pow(labData.getStacksDiameter(), 2)));
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
                    (1 - labData.getSulphurOxidesFractionAssociatedInDesulphurizationSystem() *
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
                            labData.getLowHeatValue() / 32.68) * (1 - labData.getAshRecyclingFactor()));
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
            double vm = 0.65 * Math.cbrt(V1 * dT / labData.getStacksHeight());
            double vM = 1.3 * labData.getStackAverageGasesSpeed() * labData.getStacksDiameter() / labData.getStacksHeight();

            double fe = 800 * Math.pow(vM, 3);

            if (f < 100 && fe < f) {
                f = fe;
            }

            double m = f < 100 ?
                    1.0 / (0.67 + 0.1 * Math.sqrt(f) + 0.34 * Math.cbrt(f)) :
                    1.47 / Math.cbrt(f);

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
                                vm / (1 + 0.12 * Math.sqrt(f)));

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

            if (labData.getTemperatureCoefficient() != null &&
                    labData.getHarmfulSubstancesDepositionCoefficient() != null &&
                    labData.getTerrainCoefficient() != null &&
                    labData.getNumberOfStacks() != null && labData.getWindSpeed() != null) {

                double uUm = labData.getWindSpeed() / labData.getBreakdownWindSpeed();
                double r = uUm > 1 ?
                        3 * uUm / (2 * Math.pow(uUm, 2) - uUm + 2) :
                        0.67 * uUm + 1.67 * Math.pow(uUm, 2) - 1.34 * Math.pow(uUm, 3);

                double k = labData.getTemperatureCoefficient() * labData.getHarmfulSubstancesDepositionCoefficient() *
                        n * m * labData.getTerrainCoefficient()
                        / (Math.pow(labData.getStacksHeight(), 2) * Math.cbrt(((V1 * dT))));
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
                            uUm <= 1 ? 8.42 * Math.pow((1 - uUm), 3) :
                                    0.32 * uUm + 0.68;
                    labData.setWindSpeedMaxGroundLevelConcentrationDistance(p * labData.getBwdMaxGroundLevelConcentrationDistance());
                }
            }
        }

        return labData;
    }

    @Override
    public Lab3Data createNewLabData() {
        return new Lab3Data();
    }

    @Override
    public JFreeChart createChart(Lab3Data labData, Locale locale, LabChartType chartType) {
        return isoLineChartService.createIsoLineChart(labData, locale, chartType);
    }

    @Override
    public int getLabNumber() {
        return 3;
    }

    @Override
    public Lab3Variant generateNewLabVariant() {
        Lab3Variant variant = new Lab3Variant();

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
        variant.setFuelConsumer((int) Math.round(randomUnitOutput.getUnitOutput() * 29.3 * randomUnitOutput.getBy(oil) /
                (randomFuelType.getLowHeatValue() * 1000)));
        variant.setCarbonInFlyAsh(randomFuelType.getCarbonInFlyAsh());
        variant.setSulphurContent(randomFuelType.getSulphurContent());
        variant.setAshContent(randomFuelType.getAshContent());
        variant.setWaterContent(randomFuelType.getWaterContent());
        variant.setAshRecyclingFactor(oil ? 0.0 : 92.0 + RandomUtils.nextInt(15) * 0.5);
        variant.setFlueGasNOxConcentration(randomFuelType.getFlueGasNOxConcentration());
        variant.setStackExitTemperature(randomFuelType.getStackExitTemperature());
        variant.setOutsideAirTemperature(randomCity.getOutsideAirTemperature());
        variant.setExcessAirRatio(1.4);
        variant.setAirVolume(0.0889 * (randomFuelType.getCarbonContent() + 0.375 * randomFuelType.getSulphurContent()) +
                0.0124 * randomFuelType.getHydrogenContent() - 0.0333 * randomFuelType.getOxygenContent());
        variant.setWaterVaporVolume(0.111 * randomFuelType.getHydrogenContent() +
                0.0124 * randomFuelType.getWaterContent() + 0.016 + variant.getAirVolume());
        variant.setCombustionProductsVolume((1.866 * (randomFuelType.getCarbonContent() +
                0.375 * randomFuelType.getSulphurContent()) / 100) + (0.79 * variant.getAirVolume() + 0.8 *
                randomFuelType.getNitrogenContent() / 100) + variant.getWaterVaporVolume());
        variant.setNo2BackgroundConcentration(0.05 + RandomUtils.nextInt(8) * 0.01);
        variant.setNoBackgroundConcentration(0.008 + RandomUtils.nextInt(5) * 0.01);
        variant.setSo2BackgroundConcentration(0.1 + RandomUtils.nextInt(16) * 0.01);
        variant.setAshBackgroundConcentration(0.2 + RandomUtils.nextInt(11) * 0.01);
        return variant;
    }
}
