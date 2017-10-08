package org.ekolab.client.vaadin.server.ui.customcomponents;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationResult;
import com.vaadin.ui.Component;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.service.api.ParameterCustomizer;
import org.ekolab.client.vaadin.server.service.api.ResourceService;
import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.LabVariant;
import org.ekolab.server.service.api.content.LabService;
import org.jfree.chart.encoders.ImageFormat;

import java.lang.reflect.Field;

/**
 * Created by 777Al on 08.04.2017.
 */
public class ParameterWithFormulaeLayout<BEAN extends LabData<V>, V extends LabVariant> extends ParameterLayout<BEAN,V> {
    private final String formulaePath;

    public ParameterWithFormulaeLayout(String parametersPath, Binder<BEAN> dataBinder, LabService<BEAN, V> labService, I18N i18N,
                                       ResourceService res, ParameterCustomizer parameterCustomizer) {
        super(parametersPath, dataBinder, labService, i18N, res, parameterCustomizer);
        this.formulaePath = parametersPath + "formulae/";
    }

    @Override
    protected void addSign(String fieldName, int row) {
        String resKey = fieldName + '.' + ImageFormat.PNG;
        if (res.isResourceExists(formulaePath, resKey)) {
            Component signLabelComponent = res.getImage(formulaePath, resKey);
            super.addComponent(signLabelComponent, 2, row);
        } else {
            super.addSign(fieldName, row);
        }
    }

    @Override
    protected void bindField(Field propertyField, Binder.BindingBuilder<?, ?> builder) {
        boolean validated = labService.isFieldValidated(propertyField);
        if (validated) {
            builder.withValidator((value, context) -> labService.validateFieldValue(propertyField, value, dataBinder.getBean()) ?
                    ValidationResult.ok() : ValidationResult.error(i18N.get("labwizard.wrong-value")));
        }
        super.bindField(propertyField, builder);
    }
}
