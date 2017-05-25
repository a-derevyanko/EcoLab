package org.ekolab.client.vaadin.server.ui.view.content.lab_3;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.HorizontalLayout;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ekolab.client.vaadin.server.ui.customcomponents.ParameterLayout;
import org.ekolab.server.model.content.lab3.Lab3Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.util.FieldUtils;

import java.io.IOException;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringComponent
@ViewScope
public class Step3 extends HorizontalLayout implements LabWizardStep {
    // ----------------------------- Графические компоненты --------------------------------
    @Autowired
    private I18N i18N;

    @Autowired
    private ParameterLayout<Lab3Data> firstFormLayout;

    @Autowired
    private ParameterLayout<Lab3Data> secondFormLayout;

    @Override
    public void init() throws IOException {
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
