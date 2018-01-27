package org.ekolab.server.model.content.lab2.validators;

import org.ekolab.server.common.MathUtils;
import org.ekolab.server.model.content.FieldValidationResult;
import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.lab2.Lab2Data;
import org.ekolab.server.model.content.lab2.Lab2Variant;
import org.springframework.stereotype.Service;

@Service
public class CorrectionFactorValidator implements FieldValidator<Double, Lab2Data<Lab2Variant>> {
    @Override
    public FieldValidationResult validate(Double value, Lab2Data<Lab2Variant> data) {
        return FieldValidationResult.of(data.getHemisphereSurface() == null || MathUtils.roundedCheckEquals(value,
                -10 *
                        Math.log10(1.02 *
                                (data.getBarometricPressure() * 133.0 / 100000.0) *
                                Math.sqrt(293.0 / (273.0 + data.getIndoorsTemperature()))), 1));
    }
}
