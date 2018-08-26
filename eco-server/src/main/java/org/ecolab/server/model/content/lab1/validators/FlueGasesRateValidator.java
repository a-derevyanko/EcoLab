package org.ecolab.server.model.content.lab1.validators;

import org.ecolab.server.common.MathUtils;
import org.ecolab.server.model.content.FieldValidationResult;
import org.ecolab.server.model.content.FieldValidator;
import org.ecolab.server.model.content.lab1.Lab1Data;
import org.ecolab.server.model.content.lab1.Lab1Variant;
import org.springframework.stereotype.Service;

@Service
public class FlueGasesRateValidator implements FieldValidator<Double, Lab1Data<Lab1Variant>> {

    @Override
    public FieldValidationResult validate(Double value, Lab1Data<Lab1Variant> labData) {
        return FieldValidationResult.of(labData.getFuelConsumerNormalized() == null ||
                labData.getStackExitTemperature() == null ||
                MathUtils.roundedCheckEquals(value, labData.getFuelConsumerNormalized() / 3600.0 *
                        (Constants.V0g + 1.016 * (Constants.Ayx - 1) *
                                Constants.V0) * (273 + labData.getStackExitTemperature()) / 273.0, 1));
    }
}
