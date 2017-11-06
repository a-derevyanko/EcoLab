package org.ekolab.server.model.content.lab1.validators;

import org.ekolab.server.common.MathUtils;
import org.ekolab.server.model.content.FieldValidationResult;
import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.springframework.stereotype.Service;

@Service
public class DistanceFromEmissionSourceValidator implements FieldValidator<Double, Lab1Variant, Lab1Data> {
    @Override
    public FieldValidationResult validate(Double value, Lab1Data labData) {
        return FieldValidationResult.of(labData.getVariant().getStacksHeight() == null || labData.getD() == null || labData.getF() == null ||
                MathUtils.checkEquals(value,
                        (5 - labData.getF()) / 4.0 * labData.getVariant().getStacksHeight() * labData.getD()));
    }
}
