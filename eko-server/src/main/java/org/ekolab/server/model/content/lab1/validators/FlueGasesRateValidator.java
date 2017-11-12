package org.ekolab.server.model.content.lab1.validators;

import org.ekolab.server.common.MathUtils;
import org.ekolab.server.model.content.FieldValidationResult;
import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.springframework.stereotype.Service;

@Service
public class FlueGasesRateValidator implements FieldValidator<Double, Lab1Data<Lab1Variant>> {

    @Override
    public FieldValidationResult validate(Double value, Lab1Data<Lab1Variant> labData) {
        return FieldValidationResult.of(labData.getFuelConsumerNormalized() == null ||
                labData.getStackExitTemperature() == null ||
                MathUtils.checkEquals(value, labData.getFuelConsumerNormalized() *
                        (Constants.V0g + 1.016 * (Constants.Ayx - 1) *
                                Constants.V0) * (273 + labData.getStackExitTemperature()) / 273.0));
    }
}
