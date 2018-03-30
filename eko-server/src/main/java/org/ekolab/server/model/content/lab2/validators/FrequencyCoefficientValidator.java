package org.ekolab.server.model.content.lab2.validators;

import org.ekolab.server.model.content.FieldValidationResult;
import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.lab2.Lab2Data;
import org.ekolab.server.model.content.lab2.Lab2Variant;
import org.springframework.stereotype.Service;

@Service
public class FrequencyCoefficientValidator implements FieldValidator<Double, Lab2Data<Lab2Variant>> {
    @Override
    public FieldValidationResult validate(Double value, Lab2Data<Lab2Variant> data) {
        if (data.getRoomSize() == null || data.getVariant().getEstimatedGeometricMeanFrequency() == null) {
            return FieldValidationResult.ok();
        }

        if (data.getRoomSize() < 200) {
            switch (data.getVariant().getEstimatedGeometricMeanFrequency()) {
                case F_315:
                    return FieldValidationResult.of(value == 0.8);
                case F_63:
                    return FieldValidationResult.of(value == 0.8);
                case F_125:
                    return FieldValidationResult.of(value == 0.75);
                case F_250:
                    return FieldValidationResult.of(value == 0.7);
                case F_500:
                    return FieldValidationResult.of(value == 0.8);
                case F_1000:
                    return FieldValidationResult.of(value == 1.0);
                case F_2000:
                    return FieldValidationResult.of(value == 1.4);
                case F_4000:
                    return FieldValidationResult.of(value == 1.8);
                case F_8000:
                    return FieldValidationResult.of(value == 2.5);
                default: throw new IllegalArgumentException();
            }
        } else if (data.getRoomSize() > 1000) {
            switch (data.getVariant().getEstimatedGeometricMeanFrequency()) {
                case F_315:
                    return FieldValidationResult.of(value == 0.5);
                case F_63:
                    return FieldValidationResult.of(value == 0.5);
                case F_125:
                    return FieldValidationResult.of(value == 0.5);
                case F_250:
                    return FieldValidationResult.of(value == 0.55);
                case F_500:
                    return FieldValidationResult.of(value == 0.7);
                case F_1000:
                    return FieldValidationResult.of(value == 1.0);
                case F_2000:
                    return FieldValidationResult.of(value == 1.6);
                case F_4000:
                    return FieldValidationResult.of(value == 3.0);
                case F_8000:
                    return FieldValidationResult.of(value == 6.0);
                default: throw new IllegalArgumentException();
            }
        } else {
            switch (data.getVariant().getEstimatedGeometricMeanFrequency()) {
                case F_315:
                    return FieldValidationResult.of(value == 0.65);
                case F_63:
                    return FieldValidationResult.of(value == 0.65);
                case F_125:
                    return FieldValidationResult.of(value == 0.62);
                case F_250:
                    return FieldValidationResult.of(value == 0.64);
                case F_500:
                    return FieldValidationResult.of(value == 0.75);
                case F_1000:
                    return FieldValidationResult.of(value == 1.0);
                case F_2000:
                    return FieldValidationResult.of(value == 1.5);
                case F_4000:
                    return FieldValidationResult.of(value == 2.4);
                case F_8000:
                    return FieldValidationResult.of(value == 4.2);
                default: throw new IllegalArgumentException();
            }
        }
    }
}
