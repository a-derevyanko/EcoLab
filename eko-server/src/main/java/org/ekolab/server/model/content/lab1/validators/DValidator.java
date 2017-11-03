package org.ekolab.server.model.content.lab1.validators;

import org.ekolab.server.common.MathUtils;
import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;

public class DValidator implements FieldValidator<Double, Lab1Variant, Lab1Data> {
    @Override
    public boolean validate(Double value, Lab1Data labData) {
        return labData.getVariant().getStacksHeight() == null || labData.getD() == null || labData.getF() == null ||
                MathUtils.checkEquals(value,
                        (5 - labData.getF()) / 4.0 * labData.getVariant().getStacksHeight() * labData.getD());
    }
}
