package org.ecolab.server.model.content.lab3.validators;

import org.ecolab.server.model.content.FieldValidationResult;
import org.ecolab.server.model.content.FieldValidator;
import org.ecolab.server.model.content.lab3.Lab3Data;
import org.springframework.stereotype.Service;

@Service
public class BoilerRunningTimeValidator implements FieldValidator<Double, Lab3Data> {
    @Override
    public FieldValidationResult validate(Double value, Lab3Data labData) {
        return FieldValidationResult.of(value.equals(6000.0), "lab3.error.boilerRunningTime");
    }
}
