package org.ekolab.server.service.impl.content.lab3;

import com.twelvemonkeys.image.ImageUtil;
import net.sf.dynamicreports.report.constant.ComponentPositionType;
import net.sf.dynamicreports.report.constant.ImageScale;
import net.sf.dynamicreports.report.constant.PageType;
import org.apache.commons.lang.math.RandomUtils;
import org.ekolab.server.dao.api.content.lab3.Lab3Dao;
import org.ekolab.server.model.content.LabVariant;
import org.ekolab.server.model.content.lab3.*;
import org.ekolab.server.service.api.content.lab3.IsoLineChartService;
import org.ekolab.server.service.api.content.lab3.Lab3Service;
import org.ekolab.server.service.impl.ReportTemplates;
import org.ekolab.server.service.impl.content.LabServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.Inet4Address;
import java.util.ArrayList;
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
     * @param labData данные лабораторной работы.
     * @param locale язык.
     * @return печатный вариант данных в PDF формате.
     */
    @Override
    public byte[] createReport(Lab3Data labData, Locale locale) {
        return ReportTemplates.printReport(super.createBaseLabReport(labData, 3, locale)
                .summary(cmp.image(ImageUtil.createRotated(isoLineChartService.createIsoLineChart(labData, locale).
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
            double dT = Math.abs(labData.getStackExitTemperature() - labData.getOutsideAirTemperature());
            double f = (Math.pow(labData.getStackAverageGasesSpeed(), 2) * labData.getStacksDiameter()) /
                    (Math.pow(labData.getStacksHeight(), 2) * dT);

            double V1 = labData.getCombustionProductsVolume() * labData.getFuelConsumer() / 3.6;
            double vm = 0.65 * Math.pow(V1 * dT / labData.getStacksHeight(), -3);
            double vM = 1.3 * labData.getStackAverageGasesSpeed() * labData.getStacksDiameter() / labData.getStacksHeight();

            double fe = 800 * Math.pow(vM, 3);

            if (f < 100 && fe < f) {
                f = fe;
            }

            double m = f < 100 ?
                    1.0 / (0.67 + 0.1 * Math.sqrt(f) + 0.34 * Math.pow(f, -3)) :
                    1.47 / Math.pow(f, -3);

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
                        2.48 * (1 + 0.28 * Math.pow(fe, -3)) :
                        vm < 2 ?
                                4.95 * vm * (1 + 0.28 * Math.pow(f, -3)) :
                                7 * vm * (1 + 0.28 * Math.pow(f, -3));
            }

            if (labData.getTemperatureCoefficient() != null &&
                    labData.getHarmfulSubstancesDepositionCoefficient() != null &&
                    labData.getTerrainCoefficient() != null &&
                    labData.getNumberOfStacks() != null && labData.getWindSpeed() != null) {

                double uUm = labData.getWindSpeed() / labData.getBreakdownWindSpeed();
                double r = uUm > 1 ?
                        3 * uUm / (2 * Math.pow(uUm, 2) - uUm + 2) :
                        0.67 * uUm + 1.67 * Math.pow(uUm, 2) - 1.34 * Math.pow(uUm, 3);

                if (labData.getNo2MassiveInjection() != null) {
                    labData.setBwdNo2GroundLevelConcentration(labData.getTemperatureCoefficient() *
                            labData.getNo2MassiveInjection() * labData.getHarmfulSubstancesDepositionCoefficient() *
                            n * m * labData.getBreakdownWindSpeed() * labData.getTerrainCoefficient() *
                            Math.pow((labData.getNumberOfStacks().value() / (V1 * dT)), -3) / Math.pow(labData.getStacksHeight(), 2));

                    labData.setWindSpeedMaxNoGroundLevelConcentration(labData.getBwdNoGroundLevelConcentration() * r);
                }

                if (labData.getNoMassiveInjection() != null) {
                    labData.setBwdNoGroundLevelConcentration(labData.getTemperatureCoefficient() *
                            labData.getNoMassiveInjection() * labData.getHarmfulSubstancesDepositionCoefficient() *
                            n * m * labData.getBreakdownWindSpeed() * labData.getTerrainCoefficient() *
                            Math.pow((labData.getNumberOfStacks().value() / (V1 * dT)), -3) / Math.pow(labData.getStacksHeight(), 2));

                    labData.setWindSpeedMaxNo2GroundLevelConcentration(labData.getBwdNo2GroundLevelConcentration() * r);
                }

                if (labData.getSo2MassiveInjection() != null) {
                    labData.setBwdSo2GroundLevelConcentration(labData.getTemperatureCoefficient() *
                            labData.getSo2MassiveInjection() * labData.getHarmfulSubstancesDepositionCoefficient() *
                            n * m * labData.getBreakdownWindSpeed() * labData.getTerrainCoefficient() *
                            Math.pow((labData.getNumberOfStacks().value() / (V1 * dT)), -3) / Math.pow(labData.getStacksHeight(), 2));

                    labData.setWindSpeedMaxSo2GroundLevelConcentration(labData.getBwdSo2GroundLevelConcentration() * r);
                }

                if (labData.getAshMassiveInjection() != null) {
                    labData.setBwdAshGroundLevelConcentration(labData.getTemperatureCoefficient() *
                            labData.getAshMassiveInjection() * labData.getHarmfulSubstancesDepositionCoefficient() *
                            n * m * labData.getBreakdownWindSpeed() * labData.getTerrainCoefficient() *
                            Math.pow((labData.getNumberOfStacks().value() / (V1 * dT)), -3) / Math.pow(labData.getStacksHeight(), 2));

                    labData.setWindSpeedMaxAshGroundLevelConcentration(labData.getBwdAshGroundLevelConcentration() * r);
                }

                if (labData.getBwdNoGroundLevelConcentration() != null && labData.getBwdSo2GroundLevelConcentration() != null) {
                    labData.setNoAndSo2SummationGroup(labData.getBwdNoGroundLevelConcentration() / labData.getNo2MAC() +
                            labData.getBwdSo2GroundLevelConcentration() / labData.getSo2MAC());
                }
                // todo "Если q больше 1 то должно выскакивать уведомление «Концентрация веществ обладающих суммацией вредного действия превышена»"

                if (labData.getBwdNo2GroundLevelConcentration() != null && labData.getBwdSo2GroundLevelConcentration() != null) {
                    labData.setNo2AndSo2SummationGroup(labData.getBwdNo2GroundLevelConcentration() / labData.getNo2MAC() +
                            labData.getBwdSo2GroundLevelConcentration() / labData.getSo2MAC());
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
    public byte[] printInitialData(LabVariant variant, Locale locale) {
        return printInitialData(variant, 3, locale);
    }

    @Override
    public Lab3Variant generateNewLabVariant() {
        Lab3Variant variant = new Lab3Variant();
        //todo

        //Получим случайный город, скорость ветра в нем
        City RandomCity = City.values()[RandomUtils.nextInt(City.values().length)];
        variant.setCity(RandomCity);
        variant.setWindSpeed(RandomCity.getWindSpeed());

        //Получим список типов топлива для этого города
        List<FuelType> FuelList = RandomCity.getFuelTypesForTheCity();

        //Получим случайный тип топлива из списка, низжую теплоту сгорания топлива для него
        FuelType RandomFuelType = FuelList.get(RandomUtils.nextInt(FuelList.size()));
        variant.setFuelType(RandomFuelType);
        variant.setLowHeatValue(RandomFuelType.getLowHeatValue());

        //Получим мощность 1 блока
        Integer[] UnitOutputs = {200, 300, 500, 800};
        Integer RandomUnitOutput=UnitOutputs[RandomUtils.nextInt(UnitOutputs.length)];

        int By = 0;
        List<NumberOfUnits> UnitCounts = new ArrayList<>();
        List<NumberOfStacks> StacksCounts = new ArrayList<>();
        List<Integer> StacksHeights = new ArrayList<>();
        //Получим количетсво блоков, паропроизводительность и общую мощность
        switch (RandomUnitOutput){
            case 200:
                UnitCounts.add(NumberOfUnits.SIX);
                UnitCounts.add(NumberOfUnits.EIGHT);
                variant.setSteamProductionCapacity(630);
                StacksHeights.add(120);
                StacksHeights.add(150);
                StacksHeights.add(180);
                if (RandomFuelType!=FuelType.STABILIZED_OIL & RandomFuelType!=FuelType.SULFUR_OIL) {By = 364;} else {By = 342;}
                break;
            case 300:
                UnitCounts.add(NumberOfUnits.FOUR);
                UnitCounts.add(NumberOfUnits.SIX);
                UnitCounts.add(NumberOfUnits.EIGHT);
                variant.setSteamProductionCapacity(1050);
                StacksHeights.add(120);
                StacksHeights.add(150);
                StacksHeights.add(180);
                if (RandomFuelType!=FuelType.STABILIZED_OIL & RandomFuelType!=FuelType.SULFUR_OIL) {By = 373;} else {By = 326;}
                break;
            case 500:
                UnitCounts.add(NumberOfUnits.FOUR);
                UnitCounts.add(NumberOfUnits.SIX);
                variant.setSteamProductionCapacity(1650);
                StacksHeights.add(150);
                StacksHeights.add(180);
                StacksHeights.add(210);
                if (RandomFuelType!=FuelType.STABILIZED_OIL & RandomFuelType!=FuelType.SULFUR_OIL) {By = 343;} else {By = 319;}
                break;
            case 800:
                UnitCounts.add(NumberOfUnits.TWO);
                UnitCounts.add(NumberOfUnits.FOUR);
                variant.setSteamProductionCapacity(2450);
                StacksHeights.add(180);
                StacksHeights.add(210);
                StacksHeights.add(240);
                if (RandomFuelType!=FuelType.STABILIZED_OIL & RandomFuelType!=FuelType.SULFUR_OIL) {By = 338;} else {By = 314;}
                break;
        }
        NumberOfUnits RandomUnitCount = UnitCounts.get(RandomUtils.nextInt(UnitCounts.size()));
        variant.setNumberOfUnits(RandomUnitCount);
        switch (RandomUnitCount){
            case TWO:
                StacksCounts.add(NumberOfStacks.ONE);
                StacksCounts.add(NumberOfStacks.TWO);
                break;
            case FOUR:
                StacksCounts.add(NumberOfStacks.TWO);
                break;
            case SIX:
                StacksCounts.add(NumberOfStacks.THREE);
                break;
            case EIGHT:
                StacksCounts.add(NumberOfStacks.TWO);
                StacksCounts.add(NumberOfStacks.FOUR);
                break;
        }
        NumberOfStacks RandomStacksCount = StacksCounts.get(RandomUtils.nextInt(StacksCounts.size()));
        variant.setNumberOfStacks(RandomStacksCount);
        Integer RandomStacksHeight = StacksHeights.get(RandomUtils.nextInt(StacksHeights.size()));
        variant.setStacksHeight(RandomStacksHeight);
        variant.setTppOutput(RandomUnitCount.value() * RandomUnitOutput);
        variant.setFuelConsumer((int)Math.round((RandomUnitOutput * 29.3 * By) / (RandomFuelType.getLowHeatValue() * 1000)));


        return variant;
    }
}
