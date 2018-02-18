package org.ekolab.client.vaadin.server.ui.view.content.lab_1;

import com.vaadin.ui.Alignment;
import org.ekolab.client.vaadin.server.service.api.ResourceService;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.customcomponents.ParameterWithFormulaeLayout;
import org.ekolab.client.vaadin.server.ui.view.content.LabStepWithHelp;
import org.ekolab.client.vaadin.server.ui.windows.ResourceWindow;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.springframework.security.util.FieldUtils;

/**
 * Created by 777Al on 06.04.2017.
 */
public abstract class Lab1Step2<V extends Lab1Variant> extends LabStepWithHelp<V, Lab1Data<V>> {

    // ----------------------------- Графические компоненты --------------------------------
    private final ParameterWithFormulaeLayout<Lab1Data<V>, V> firstFormLayout;

    public Lab1Step2(I18N i18N, ResourceWindow resourceWindow, ResourceService resourceService, ParameterWithFormulaeLayout<Lab1Data<V>, V> firstFormLayout) {
        super(i18N, resourceWindow, resourceService);
        this.firstFormLayout = firstFormLayout;
    }

    @Override
    public void init() {
        super.init();
        setSizeFull();
        setMargin(true);
        addComponent(firstFormLayout);
        setComponentAlignment(firstFormLayout, Alignment.MIDDLE_CENTER);
        firstFormLayout.setCaption(i18N.get("lab1.step1.data-normalization"));
        firstFormLayout.addField(FieldUtils.getField(Lab1Data.class, "excessAirRatio"));
        firstFormLayout.addField(FieldUtils.getField(Lab1Data.class, "flueGasNOxConcentrationNC"));
        firstFormLayout.addField(FieldUtils.getField(Lab1Data.class, "excessOfNorms"));
        firstFormLayout.addField(FieldUtils.getField(Lab1Data.class, "flueGasesRate"));
        firstFormLayout.addField(FieldUtils.getField(Lab1Data.class, "dryGasesFlowRate"));
        firstFormLayout.addField(FieldUtils.getField(Lab1Data.class, "massEmissions"));
        firstFormLayout.addField(FieldUtils.getField(Lab1Data.class, "flueGasesSpeed"));
    }
}
