package org.ekolab.server.model.content.lab1.validators;

import org.ekolab.server.common.MathUtils;
import org.ekolab.server.model.content.FieldValidationResult;
import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.springframework.stereotype.Service;

@Service
public class FlueGasesSpeedValidator implements FieldValidator<Double, Lab1Variant, Lab1Data> {
    @Override
    public FieldValidationResult validate(Double value, Lab1Data labData) {
        return FieldValidationResult.of(labData.getFlueGasesRate() == null || labData.getVariant().getStacksDiameter() == null ||
                MathUtils.checkEquals(value, 4 *
                        labData.getFlueGasesRate() / (Math.PI * labData.getVariant().getStacksDiameter())));
    }
}
