package org.ekolab.server.model.content.lab3.validators;

import org.ekolab.server.model.content.FieldValidationResult;
import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.lab3.Lab3Data;
import org.ekolab.server.model.content.lab3.Lab3Variant;
import org.springframework.stereotype.Service;

@Service
public class StacksDiameterValidator implements FieldValidator<Double, Lab3Variant, Lab3Data> {
    @Override
    public FieldValidationResult validate(Double value, Lab3Data labData) {
        if (labData.getCombustionProductsVolume() != null && labData.getFuelConsumer() != null && labData.getNumberOfUnits() != null && labData.getNumberOfStacks() != null) {
            double stackAverageGasesSpeed = ((4 * labData.getCombustionProductsVolume() * labData.getFuelConsumer() * labData.getNumberOfUnits().value()) /
                    (3.6 * Math.PI * Math.pow(value, 2) * labData.getNumberOfStacks().value()));

            if (stackAverageGasesSpeed < 10.0) {
                return FieldValidationResult.error("lab3.error.stackAverageGasesSpeed-1");
            } else if (stackAverageGasesSpeed > 25.0) {
                return FieldValidationResult.error("lab3.error.stackAverageGasesSpeed-2");
            }
        }
        return FieldValidationResult.ok();
    }
}
