package org.ecolab.server.model.content.lab1.validators;

import org.ecolab.server.common.MathUtils;
import org.ecolab.server.model.content.FieldValidationResult;
import org.ecolab.server.model.content.FieldValidator;
import org.ecolab.server.model.content.lab1.Lab1Data;
import org.ecolab.server.model.content.lab1.Lab1Variant;
import org.springframework.stereotype.Service;

@Service
public class NValidator implements FieldValidator<Double, Lab1Data<Lab1Variant>> {
    @Override
    public FieldValidationResult validate(Double value, Lab1Data<Lab1Variant> labData) {
        return FieldValidationResult.of(labData.getU() == null || MathUtils.roundedCheckEquals(value,
                labData.getU() >= 2.0 ?
                        1.0 : labData.getU() > 0.5 ? 0.532 * Math.pow(labData.getU(), 2) - 2.13 * labData.getU() + 3.13 :
                        4.4 * labData.getU(), 2));
    }
}
