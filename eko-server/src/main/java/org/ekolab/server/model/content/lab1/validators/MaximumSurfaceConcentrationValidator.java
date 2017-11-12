package org.ekolab.server.model.content.lab1.validators;

import org.ekolab.server.common.MathUtils;
import org.ekolab.server.model.content.FieldValidationResult;
import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.springframework.stereotype.Service;

@Service
public class MaximumSurfaceConcentrationValidator implements FieldValidator<Double, Lab1Data<Lab1Variant>> {
    @Override
    public FieldValidationResult validate(Double value, Lab1Data<Lab1Variant> labData) {
        if (labData.getStackExitTemperature() == null || labData.getOutsideAirTemperature() == null ||
                labData.getTemperatureCoefficient() == null || labData.getMassEmissions() == null ||
                labData.getHarmfulSubstancesDepositionCoefficient() == null || labData.getM() == null || labData.getN() == null ||
                labData.getTerrainCoefficient() == null || labData.getStacksHeight() == null ||
                labData.getFlueGasesRate() == null) {
            return FieldValidationResult.ok();
        }
        double dT = labData.getStackExitTemperature() - labData.getOutsideAirTemperature();
        return FieldValidationResult.of(MathUtils.checkEquals(value,
                labData.getTemperatureCoefficient() * labData.getMassEmissions() *
                        labData.getHarmfulSubstancesDepositionCoefficient() * labData.getM() * labData.getN() *
                        labData.getTerrainCoefficient() / Math.pow(labData.getStacksHeight(), 2) *
                        Math.cbrt(1.0 / (labData.getFlueGasesRate() * dT))));
    }
}
