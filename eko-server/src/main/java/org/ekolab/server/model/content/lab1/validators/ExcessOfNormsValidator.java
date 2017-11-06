package org.ekolab.server.model.content.lab1.validators;

import org.ekolab.server.common.MathUtils;
import org.ekolab.server.model.content.FieldValidationResult;
import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.springframework.stereotype.Service;

@Service
public class ExcessOfNormsValidator implements FieldValidator<Boolean, Lab1Variant, Lab1Data> {
    @Override
    public FieldValidationResult validate(Boolean value, Lab1Data labData) {
        return FieldValidationResult.of(labData.getFlueGasNOxConcentrationNC() == null || value == MathUtils.checkEquals(125.0,
                labData.getFlueGasNOxConcentrationNC()));
    }
}
