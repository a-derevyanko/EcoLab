package org.ekolab.client.vaadin.server.ui.view.content.lab_2;

import com.vaadin.data.Binder;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ekolab.client.vaadin.server.ui.customcomponents.ParameterWithFormulaeLayout;
import org.ekolab.server.model.content.lab2.Lab2Data;
import org.ekolab.server.model.content.lab2.Lab2Variant;
import org.springframework.security.util.FieldUtils;

/**
 * Created by 777Al on 06.04.2017.
 */
public abstract class Lab2Step2<V extends Lab2Variant> extends HorizontalLayout implements LabWizardStep<Lab2Data<V>, V> {
    private final I18N i18N;

    private final Binder<Lab2Data<V>> binder;

    private final ParameterWithFormulaeLayout<Lab2Data<V>, V> firstFormLayout;

    // ----------------------------- Графические компоненты --------------------------------
    private final Label nameLabel = new Label();

    protected Lab2Step2(I18N i18N,
                     Binder<Lab2Data<V>> binder,
                     ParameterWithFormulaeLayout<Lab2Data<V>, V> firstFormLayout) {
        this.i18N = i18N;
        this.binder = binder;
        this.firstFormLayout = firstFormLayout;
    }

    @Override
    public void init() {
        LabWizardStep.super.init();
        setSizeFull();
        setMargin(true);
        addComponent(firstFormLayout);
        setCaption(i18N.get("lab2.step2.general-data"));

        firstFormLayout.addField(FieldUtils.getField(Lab2Data.class, "correctionFactor"));
        firstFormLayout.addField(FieldUtils.getField(Lab2Data.class, "hemisphereSurface"));
        firstFormLayout.addField(FieldUtils.getField(Lab2Data.class, "measuringFactor"));
        firstFormLayout.addField(FieldUtils.getField(Lab2Data.class, "roomConstant1000"));
        firstFormLayout.addField(FieldUtils.getField(Lab2Data.class, "roomConstant"));

        firstFormLayout.addField(FieldUtils.getField(Lab2Data.class, "soundPressureMeasuringSurface"));
        firstFormLayout.addField(FieldUtils.getField(Lab2Data.class, "soundPowerLevel"));
        firstFormLayout.addField(FieldUtils.getField(Lab2Data.class, "frequencyCoefficient"));
        firstFormLayout.addField(FieldUtils.getField(Lab2Data.class, "reflectedSoundPower"));
    }

    @Override
    public void beforeEnter() {
        nameLabel.setValue(i18N.get(binder.getBean().getVariant().getName()));
    }
}
