package org.ecolab.server.model.content.lab1.validators;

import org.ecolab.server.common.MathUtils;
import org.ecolab.server.model.content.FieldValidationResult;
import org.ecolab.server.model.content.FieldValidator;
import org.ecolab.server.model.content.lab1.Lab1Data;
import org.ecolab.server.model.content.lab1.Lab1Variant;
import org.springframework.stereotype.Service;

@Service
public class ExcessOfNormsValidator implements FieldValidator<Boolean, Lab1Data<Lab1Variant>> {
    @Override
    public FieldValidationResult validate(Boolean value, Lab1Data<Lab1Variant> labData) {
        return FieldValidationResult.of(labData.getFlueGasNOxConcentrationNC() == null || value == MathUtils.roundedCheckEquals(125.0,
                labData.getFlueGasNOxConcentrationNC(), 2));
    }
}
