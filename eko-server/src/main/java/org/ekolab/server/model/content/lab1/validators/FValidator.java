package org.ekolab.server.model.content.lab1.validators;

import org.ekolab.server.common.MathUtils;
import org.ekolab.server.model.content.FieldValidationResult;
import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.springframework.stereotype.Service;

@Service
public class FValidator implements FieldValidator<Double, Lab1Variant, Lab1Data> {
    @Override
    public FieldValidationResult validate(Double value, Lab1Data labData) {
        if (labData.getVariant().getStackExitTemperature() == null || labData.getVariant().getOutsideAirTemperature() == null ||
                labData.getFlueGasesSpeed() == null || labData.getVariant().getStacksDiameter() == null ||
                labData.getVariant().getStacksHeight() == null) {
            return FieldValidationResult.ok();
        }

        double dT = labData.getVariant().getStackExitTemperature() - labData.getVariant().getOutsideAirTemperature();
        return FieldValidationResult.of(MathUtils.checkEquals(value, 1000 * labData.getFlueGasesSpeed() * labData.getVariant().getStacksDiameter() /
                (Math.pow(labData.getVariant().getStacksHeight(), 2) * dT)));
    }
}
