package org.ekolab.server.model.content.lab1.validators;

import org.ekolab.server.common.MathUtils;
import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;

public class DryGasesFlowRateValidator implements FieldValidator<Double, Lab1Variant, Lab1Data> {

    @Override
    public boolean validate(Double value, Lab1Data labData) {
        return labData.getVariant().getFuelConsumerNormalized() == null ||
                labData.getExcessAirRatio() == null ||
                MathUtils.checkEquals((Double) value, labData.getVariant().getFuelConsumerNormalized() *
                        (Constants.V0g + (labData.getExcessAirRatio() - 1) * Constants.V0 - Constants.V_H2O));
    }
}
