package org.ekolab.server.model.content.lab1.validators;

import org.ekolab.server.common.MathUtils;
import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;

public class ExcessOfNormsValidator implements FieldValidator<Boolean, Lab1Variant, Lab1Data> {
    @Override
    public boolean validate(Boolean value, Lab1Data labData) {
        return labData.getFlueGasNOxConcentrationNC() == null || value == MathUtils.checkEquals(125.0,
                labData.getFlueGasNOxConcentrationNC());
    }
}
