package org.ekolab.server.model.content.lab1.validators;

import org.ekolab.server.common.MathUtils;
import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;

public class NValidator implements FieldValidator<Lab1Variant, Lab1Data> {
    @Override
    public boolean validate(Object value, Lab1Data labData) {
        return labData.getU() == null || MathUtils.checkEquals((Double) value,
                labData.getU() >= 2.0 ?
                        1.0 : labData.getU() > 0.5 ? 0.532 * Math.pow(labData.getU(), 2) - 2.13 * labData.getU() + 3.13 :
                        4.4 * labData.getU());
    }
}
