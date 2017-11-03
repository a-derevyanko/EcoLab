package org.ekolab.server.model.content.lab1.validators;

import org.ekolab.server.common.MathUtils;
import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;

public class MValidator3 implements FieldValidator<Lab1Variant, Lab1Data> {
    @Override
    public boolean validate(Object value, Lab1Data labData) {
        return labData.getF() == null ||
                MathUtils.checkEquals((Double) value, 1.0 /
                        (0.67 + 0.1 * Math.sqrt(labData.getF()) + 0.34 * Math.cbrt(labData.getF())));
    }
}
