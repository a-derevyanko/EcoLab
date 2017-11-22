package org.ekolab.server.model.content.lab1.validators;

import org.apache.commons.math3.util.Precision;
import org.ekolab.server.common.MathUtils;
import org.ekolab.server.model.content.FieldValidationResult;
import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.springframework.stereotype.Service;

@Service
public class ExcessAirRatioValidator implements FieldValidator<Double, Lab1Data<Lab1Variant>> {
    @Override
    public FieldValidationResult validate(Double value, Lab1Data<Lab1Variant> labData) {
        return FieldValidationResult.of(labData.getOxygenConcentrationPoint() == null || MathUtils.roundedCheckEquals(value,
                Precision.round(21.0 / (21.0 - labData.getOxygenConcentrationPoint()), 2), 2));
    }
}
