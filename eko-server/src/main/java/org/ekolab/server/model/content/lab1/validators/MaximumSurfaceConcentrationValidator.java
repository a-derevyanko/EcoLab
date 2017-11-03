package org.ekolab.server.model.content.lab1.validators;

import org.ekolab.server.common.MathUtils;
import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;

public class MaximumSurfaceConcentrationValidator implements FieldValidator<Lab1Variant, Lab1Data> {
    @Override
    public boolean validate(Object value, Lab1Data labData) {
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
}
