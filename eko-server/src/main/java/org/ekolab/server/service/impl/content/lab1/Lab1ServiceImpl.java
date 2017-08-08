package org.ekolab.server.service.impl.content.lab1;

import org.apache.commons.lang.math.RandomUtils;
import org.ekolab.server.common.MathUtils;
import org.ekolab.server.dao.api.content.lab1.Lab1Dao;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.ekolab.server.service.api.content.LabChartType;
import org.ekolab.server.service.api.content.lab1.Lab1Service;
import org.ekolab.server.service.impl.content.LabServiceImpl;
import org.jfree.chart.JFreeChart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Created by 777Al on 26.04.2017.
 */
@Service
public class Lab1ServiceImpl extends LabServiceImpl<Lab1Data, Lab1Variant> implements Lab1Service {
    private static final double V0g = 10.88;
    private static final double V0 = 9.68;
    private static final double Ayx = 1.4;
    private static final double V_H2O = 2.1;

    @Autowired
    public Lab1ServiceImpl(Lab1Dao lab1Dao) {
        super(lab1Dao);
    }

    @Override
    public byte[] createReport(Lab1Data labData, Locale locale) {
        return new byte[0];
    }

    @Override
    public Lab1Data updateCalculatedFields(Lab1Data labData) {
        //todo correctionFactor!!!!
        return null;
    }

    @Override
    public Lab1Data createNewLabData() {
        return new Lab1Data();
    }

    @Override
    public JFreeChart createChart(Lab1Data labData, Locale locale, LabChartType chartType) {
        return null;
    }

    @Override
    public int getLabNumber() {
        return 1;
    }

    @Override
    protected Lab1Variant generateNewLabVariant() {
        Lab1Variant variant = new Lab1Variant();

        variant.setOutsideAirTemperature(-25 + RandomUtils.nextInt(11) * 5);
        variant.setSteamProductionCapacity(30 + RandomUtils.nextInt(5)*5);

        variant.setOxygenConcentrationPoint(5.0 + RandomUtils.nextInt(21) * 0.1);

        variant.setFuelConsumerNormalized(variant.getSteamProductionCapacity() * 80 + 100);
        variant.setStackExitTemperature(120 + RandomUtils.nextInt(26));

        variant.setFlueGasNOxConcentration(120 + RandomUtils.nextInt(17) * 5);
        return variant;
    }

    @Override
    public Lab1Data startNewLabWithEmptyVariant(String userName) {
        Lab1Data lab1Data = createBaseLabData(userName);
        lab1Data.setVariant(new Lab1Variant());
        labDao.saveLab(lab1Data);
        return lab1Data;
    }

    protected boolean validateFieldValue(String fieldName, Object value, Lab1Data labData) {
        switch (fieldName) {
            case "excessAirRatio": {
                return MathUtils.checkEquals((Double) value,
                        21.0 / (21.0 - labData.getVariant().getOxygenConcentrationPoint()));
            }
            case "flueGasNOxConcentrationNC": {
                return labData.getVariant().getFlueGasNOxConcentration() == null || labData.getExcessAirRatio() == null ||
                        MathUtils.checkEquals((Double) value,
                                labData.getVariant().getFlueGasNOxConcentration() * labData.getExcessAirRatio() / 1.4);
            }
            case "excessOfNorms": {
                return labData.getFlueGasNOxConcentrationNC() == null || (Boolean) value == MathUtils.checkEquals(125.0,
                        labData.getFlueGasNOxConcentrationNC());
            }
            case "flueGasesRate": {
                return labData.getVariant().getFuelConsumerNormalized() == null ||
                        labData.getVariant().getStackExitTemperature() == null ||
                        MathUtils.checkEquals((Double) value, labData.getVariant().getFuelConsumerNormalized() *
                                (V0g + 1.016 * (Ayx - 1) * V0) * (273 + labData.getVariant().getStackExitTemperature()) / 273.0);
            }
            case "dryGasesFlowRate": {
                return labData.getVariant().getFuelConsumerNormalized() == null ||
                        labData.getExcessAirRatio() == null ||
                        MathUtils.checkEquals((Double) value, labData.getVariant().getFuelConsumerNormalized() *
                                (V0g + (labData.getExcessAirRatio() - 1) * V0 - V_H2O));
            }
            case "massEmissions": {
                return labData.getVariant().getFlueGasNOxConcentration() == null || labData.getDryGasesFlowRate() == null ||
                        MathUtils.checkEquals((Double) value,
                                labData.getVariant().getFlueGasNOxConcentration() * labData.getDryGasesFlowRate());
            }
            case "flueGasesSpeed": {
                return labData.getFlueGasesRate() == null || labData.getVariant().getStacksDiameter() == null ||
                        MathUtils.checkEquals((Double) value, 4 *
                                labData.getFlueGasesRate() / (Math.PI * labData.getVariant().getStacksDiameter()));
            }
            case "f": {
                if (labData.getVariant().getStackExitTemperature() == null || labData.getVariant().getOutsideAirTemperature() == null ||
                        labData.getFlueGasesSpeed() == null || labData.getVariant().getStacksDiameter() == null ||
                        labData.getVariant().getStacksHeight() == null) {
                    return true;
                }

                double dT = labData.getVariant().getStackExitTemperature() - labData.getVariant().getOutsideAirTemperature();
                return MathUtils.checkEquals((Double) value, 1000 * labData.getFlueGasesSpeed() * labData.getVariant().getStacksDiameter() /
                        (Math.pow(labData.getVariant().getStacksHeight(), 2) * dT));
            }
            case "m": {
                return labData.getF() == null ||
                        MathUtils.checkEquals((Double) value, 1.0 /
                                (0.67 + 0.1 * Math.sqrt(labData.getF()) + 0.34 * Math.cbrt(labData.getF())));
            }
            case "u": {
                if (labData.getVariant().getStackExitTemperature() == null || labData.getVariant().getOutsideAirTemperature() == null ||
                        labData.getFlueGasesRate() == null || labData.getVariant().getStacksHeight() == null) {
                    return true;
                }
                double dT = labData.getVariant().getStackExitTemperature() - labData.getVariant().getOutsideAirTemperature();
                return MathUtils.checkEquals((Double) value, 0.65 *
                        Math.cbrt(labData.getFlueGasesRate() * dT / labData.getVariant().getStacksHeight()));
            }
            case "n": {
                return labData.getU() == null || MathUtils.checkEquals((Double) value,
                        labData.getU() >= 2.0 ?
                                1.0 : labData.getU() > 0.5 ? 0.532 * Math.pow(labData.getU(), 2) - 2.13 * labData.getU() + 3.13 :
                                4.4 * labData.getU());
            }
            case "d": {
                return labData.getU() == null || labData.getF() == null || MathUtils.checkEquals((Double) value,
                        7 * labData.getU() * (1.0 + 0.28 * Math.sqrt(labData.getF())));
            }
            case "distanceFromEmissionSource": {
                return labData.getVariant().getStacksHeight() == null || labData.getD() == null || labData.getF() == null ||
                        MathUtils.checkEquals((Double) value,
                                (5 - labData.getF()) / 4.0 * labData.getVariant().getStacksHeight() * labData.getD());
            }
            case "maximumSurfaceConcentration": {
                if (labData.getVariant().getStackExitTemperature() == null || labData.getVariant().getOutsideAirTemperature() == null ||
                        labData.getTemperatureCoefficient() == null || labData.getMassEmissions() == null ||
                        labData.getHarmfulSubstancesDepositionCoefficient() == null || labData.getM() == null || labData.getN() == null ||
                        labData.getTerrainCoefficient() == null || labData.getVariant().getStacksHeight() == null ||
                        labData.getFlueGasesRate() == null) {
                    return true;
                }
                double dT = labData.getVariant().getStackExitTemperature() - labData.getVariant().getOutsideAirTemperature();
                return MathUtils.checkEquals((Double) value,
                        labData.getTemperatureCoefficient() * labData.getMassEmissions() *
                                labData.getHarmfulSubstancesDepositionCoefficient() * labData.getM() * labData.getN() *
                                labData.getTerrainCoefficient() / Math.pow(labData.getVariant().getStacksHeight(), 2) *
                                Math.cbrt(1.0 / (labData.getFlueGasesRate() * dT)));
            }
            default:
                return super.validateFieldValue(fieldName, value, labData);
        }
    }
}
