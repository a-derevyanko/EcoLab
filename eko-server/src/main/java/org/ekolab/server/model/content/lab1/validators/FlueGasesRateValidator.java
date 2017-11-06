package org.ekolab.server.model.content.lab1.validators;

import org.ekolab.server.common.MathUtils;
import org.ekolab.server.model.content.FieldValidationResult;
import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.springframework.stereotype.Service;

@Service
public class FlueGasesRateValidator implements FieldValidator<Double, Lab1Variant, Lab1Data> {

    @Override
    public FieldValidationResult validate(Double value, Lab1Data labData) {
        return FieldValidationResult.of(labData.getVariant().getFuelConsumerNormalized() == null ||
                labData.getVariant().getStackExitTemperature() == null ||
                MathUtils.checkEquals(value, labData.getVariant().getFuelConsumerNormalized() *
                        (Constants.V0g + 1.016 * (Constants.Ayx - 1) *
                                Constants.V0) * (273 + labData.getVariant().getStackExitTemperature()) / 273.0));
    }
}
