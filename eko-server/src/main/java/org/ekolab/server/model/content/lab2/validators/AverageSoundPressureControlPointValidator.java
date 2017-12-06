package org.ekolab.server.model.content.lab2.validators;

import org.ekolab.server.model.content.FieldValidationResult;
import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.lab2.Lab2Data;
import org.ekolab.server.model.content.lab2.Lab2Variant;

import java.util.List;

public class AverageSoundPressureControlPointValidator implements FieldValidator<List<Double>, Lab2Data<Lab2Variant>> {
    @Override
    public FieldValidationResult validate(List<Double> value, Lab2Data<Lab2Variant> data) {
        for (int i = 1; i < value.size(); i++) {
            if (value.get(i) < value.get(i - 1)) {
                return FieldValidationResult.error("lab2.error.soundPressureControlPointValidator");
            }
        }
        return FieldValidationResult.ok();
    }
}
