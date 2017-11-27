package org.ekolab.client.vaadin.server.ui.view.content.lab_3;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.HorizontalLayout;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ekolab.client.vaadin.server.ui.customcomponents.ParameterLayout;
import org.ekolab.server.model.content.lab3.Lab3Data;
import org.ekolab.server.model.content.lab3.Lab3Variant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.util.FieldUtils;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringComponent
@ViewScope
public class Lab3Step3 extends HorizontalLayout implements LabWizardStep<Lab3Data, Lab3Variant> {
    // ----------------------------- Графические компоненты --------------------------------
    private final I18N i18N;

    private final ParameterLayout<Lab3Data, Lab3Variant> firstFormLayout;

    private final ParameterLayout<Lab3Data, Lab3Variant> secondFormLayout;

    @Autowired
    public Lab3Step3(I18N i18N, ParameterLayout<Lab3Data, Lab3Variant> firstFormLayout, ParameterLayout<Lab3Data, Lab3Variant> secondFormLayout) {
        this.i18N = i18N;
        this.firstFormLayout = firstFormLayout;
        this.secondFormLayout = secondFormLayout;
    }

    @Override
    public void init() {
        LabWizardStep.super.init();
        setSizeFull();
        setMargin(true);
        addComponent(firstFormLayout);
        addComponent(secondFormLayout);
        firstFormLayout.setCaption(i18N.get("lab3.step3.calc-results"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "stackAverageGasesSpeed"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "noxMassiveInjection"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "no2MassiveInjection"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "noMassiveInjection"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "so2MassiveInjection"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "ashMassiveInjection"));

        secondFormLayout.setCaption(i18N.get("lab3.step3.MPC-input"));
        secondFormLayout.addField(FieldUtils.getField(Lab3Data.class, "temperatureCoefficient"));
        secondFormLayout.addField(FieldUtils.getField(Lab3Data.class, "terrainCoefficient"));
        secondFormLayout.addField(FieldUtils.getField(Lab3Data.class, "harmfulSubstancesDepositionCoefficient"));
        secondFormLayout.addField(FieldUtils.getField(Lab3Data.class, "no2MAC"));
        secondFormLayout.addField(FieldUtils.getField(Lab3Data.class, "noMAC"));
        secondFormLayout.addField(FieldUtils.getField(Lab3Data.class, "so2MAC"));
        secondFormLayout.addField(FieldUtils.getField(Lab3Data.class, "ashMAC"));
    }
}
