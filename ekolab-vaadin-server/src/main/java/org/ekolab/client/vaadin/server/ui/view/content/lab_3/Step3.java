package org.ekolab.client.vaadin.server.ui.view.content.lab_3;

import com.vaadin.server.CompositeErrorMessage;
import com.vaadin.server.ErrorMessage;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.HorizontalLayout;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ekolab.client.vaadin.server.ui.customcomponents.ParameterLayout;
import org.ekolab.server.model.Lab3Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.util.FieldUtils;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Override
    public ErrorMessage getComponentError() {
        Set<ErrorMessage> messageList = Stream.of(super.getComponentError(),
                firstFormLayout.getComponentError(),  secondFormLayout.getComponentError())
                .filter(Objects::nonNull).collect(Collectors.toSet());
        return messageList.isEmpty() ? null : new CompositeErrorMessage(messageList);
    }
}
