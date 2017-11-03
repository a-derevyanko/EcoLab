package org.ekolab.server.model.content.lab1.validators;

import org.ekolab.server.common.MathUtils;
import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;

public class FlueGasesSpeedValidator implements FieldValidator<Lab1Variant, Lab1Data> {
    @Override
    public boolean validate(Object value, Lab1Data labData) {
        return labData.getFlueGasesRate() == null || labData.getVariant().getStacksDiameter() == null ||
                MathUtils.checkEquals((Double) value, 4 *
                        labData.getFlueGasesRate() / (Math.PI * labData.getVariant().getStacksDiameter()));
    }
}
