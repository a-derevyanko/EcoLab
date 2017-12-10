package org.ekolab.server.model.content.lab2.validators;

import org.ekolab.server.model.content.FieldValidationResult;
import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.lab2.Lab2Data;
import org.ekolab.server.model.content.lab2.Lab2Variant;
import org.springframework.stereotype.Service;

@Service
public class QuantityOfSingleTypeEquipmentValidator implements FieldValidator<Integer, Lab2Data<Lab2Variant>> {
    @Override
    public FieldValidationResult validate(Integer value, Lab2Data<Lab2Variant> data) {
        return FieldValidationResult.of(value == null || value > 0 && value <= 5, "lab2.error.quantityOfSingleTypeEquipment");
    }
}
