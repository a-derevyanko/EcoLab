package org.ekolab.server.model.content.lab1.validators;

import org.ekolab.server.common.MathUtils;
import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;

public class FlueGasesRateValidator implements FieldValidator<Double, Lab1Variant, Lab1Data> {

    @Override
    public boolean validate(Double value, Lab1Data labData) {
        return labData.getVariant().getFuelConsumerNormalized() == null ||
                labData.getVariant().getStackExitTemperature() == null ||
                MathUtils.checkEquals(value, labData.getVariant().getFuelConsumerNormalized() *
                        (Constants.V0g + 1.016 * (Constants.Ayx - 1) * Constants.V0) * (273 + labData.getVariant().getStackExitTemperature()) / 273.0);
    }
}
