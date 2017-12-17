package org.ekolab.server.model.content.lab2.validators;

import org.ekolab.server.common.MathUtils;
import org.ekolab.server.model.content.FieldValidationResult;
import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.lab2.Lab2Data;
import org.ekolab.server.model.content.lab2.Lab2Variant;

public class ReflectedSoundPowerValidator implements FieldValidator<Double, Lab2Data<Lab2Variant>> {
    @Override
    public FieldValidationResult validate(Double value, Lab2Data<Lab2Variant> data) {
        return FieldValidationResult.of(data.getSoundPowerLevel() == null ||
                data.getQuantityOfSingleTypeEquipment() == null || data.getRoomConstant() == null ||
                MathUtils.roundedCheckEquals(value, data.getSoundPowerLevel() + 10 *
                        Math.log10(data.getQuantityOfSingleTypeEquipment()) - 10 * Math.log10(data.getRoomConstant()) + 6, 1));
    }
}
