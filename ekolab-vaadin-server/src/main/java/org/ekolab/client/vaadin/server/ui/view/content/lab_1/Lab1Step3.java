package org.ekolab.client.vaadin.server.ui.view.content.lab_1;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ekolab.client.vaadin.server.ui.customcomponents.ParameterWithFormulaeLayout;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.springframework.security.util.FieldUtils;

/**
 * Created by 777Al on 06.04.2017.
 */
public abstract class Lab1Step3<V extends Lab1Variant> extends HorizontalLayout implements LabWizardStep<Lab1Data<V>, V> {
    // ----------------------------- Графические компоненты --------------------------------
    private final I18N i18N;

    private final ParameterWithFormulaeLayout<Lab1Data<V>, V> firstFormLayout;

    public Lab1Step3(I18N i18N, ParameterWithFormulaeLayout<Lab1Data<V>, V> firstFormLayout) {
        this.i18N = i18N;
        this.firstFormLayout = firstFormLayout;
    }

    @Override
    public void init() {
        LabWizardStep.super.init();
        setSizeFull();
        setMargin(true);
        addComponent(firstFormLayout);
        setComponentAlignment(firstFormLayout, Alignment.MIDDLE_CENTER);
        firstFormLayout.setCaption(i18N.get("lab1.step3.max-concentration-calculation"));
        firstFormLayout.addField(FieldUtils.getField(Lab1Data.class, "f"));
        firstFormLayout.addField(FieldUtils.getField(Lab1Data.class, "m"));
        firstFormLayout.addField(FieldUtils.getField(Lab1Data.class, "u"));
        firstFormLayout.addField(FieldUtils.getField(Lab1Data.class, "n"));
        firstFormLayout.addField(FieldUtils.getField(Lab1Data.class, "d"));
        firstFormLayout.addField(FieldUtils.getField(Lab1Data.class, "harmfulSubstancesDepositionCoefficient"));
        firstFormLayout.addField(FieldUtils.getField(Lab1Data.class, "terrainCoefficient"));
        firstFormLayout.addField(FieldUtils.getField(Lab1Data.class, "temperatureCoefficient"));
        firstFormLayout.addField(FieldUtils.getField(Lab1Data.class, "distanceFromEmissionSource"));
        firstFormLayout.addField(FieldUtils.getField(Lab1Data.class, "maximumSurfaceConcentration"));
    }
}
