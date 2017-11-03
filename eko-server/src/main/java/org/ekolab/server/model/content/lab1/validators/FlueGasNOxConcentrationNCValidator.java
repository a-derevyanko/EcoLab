package org.ekolab.server.model.content.lab1.validators;

import org.ekolab.server.common.MathUtils;
import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;

public class FlueGasNOxConcentrationNCValidator implements FieldValidator<Lab1Variant, Lab1Data> {
    @Override
    public boolean validate(Object value, Lab1Data labData) {
        return labData.getVariant().getFlueGasNOxConcentration() == null || labData.getExcessAirRatio() == null ||
                MathUtils.checkEquals((Double) value,
                        labData.getVariant().getFlueGasNOxConcentration() * labData.getExcessAirRatio() / 1.4);
    }
}
