package org.ekolab.server.model.content.lab2.validators;

import org.ekolab.server.common.MathUtils;
import org.ekolab.server.model.content.FieldValidationResult;
import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.lab2.Lab2Data;
import org.ekolab.server.model.content.lab2.Lab2Variant;
import org.springframework.stereotype.Service;

@Service
public class RoomConstantValidator implements FieldValidator<Double, Lab2Data<Lab2Variant>> {
    @Override
    public FieldValidationResult validate(Double value, Lab2Data<Lab2Variant> data) {
        return FieldValidationResult.of(data.getRoomConstant1000() == null || data.getFrequencyCoefficient() == null ||
                MathUtils.roundedCheckEquals(value, data.getRoomConstant1000() * data.getFrequencyCoefficient(), 1));
    }
}
