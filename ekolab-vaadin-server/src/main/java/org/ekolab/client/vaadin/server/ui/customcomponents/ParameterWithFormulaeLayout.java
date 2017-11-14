package org.ekolab.client.vaadin.server.ui.customcomponents;

import com.vaadin.data.Binder;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import org.ekolab.client.vaadin.server.service.api.ParameterCustomizer;
import org.ekolab.client.vaadin.server.service.api.ResourceService;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.windows.ResourceWindow;
import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.LabVariant;
import org.ekolab.server.service.api.content.LabService;
import org.ekolab.server.service.api.content.ValidationService;
import org.jfree.chart.encoders.ImageFormat;

/**
 * Created by 777Al on 08.04.2017.
 */
public class ParameterWithFormulaeLayout<BEAN extends LabData<V>, V extends LabVariant> extends ParameterLayout<BEAN,V> {
    private final String formulaePath;

    public ParameterWithFormulaeLayout(String parametersPath, Binder<BEAN> dataBinder, LabService<BEAN, V> labService, I18N i18N,
                                       ResourceService res, ParameterCustomizer parameterCustomizer,
                                       ValidationService validationService, ResourceWindow resourceWindow) {
        super(parametersPath, dataBinder, labService, i18N, res, parameterCustomizer, validationService, resourceWindow);
        this.formulaePath = parametersPath + "formulae/";
    }

    @Override
    protected void addSign(String fieldName, int row) {
        String resKey = fieldName + '.' + ImageFormat.PNG;
        if (res.isResourceExists(formulaePath, resKey)) {
            Component signLabelComponent = new Image(null, res.getImage(formulaePath, resKey));
            super.addComponent(signLabelComponent, 2, row);
        } else {
            super.addSign(fieldName, row);
        }
    }
}
