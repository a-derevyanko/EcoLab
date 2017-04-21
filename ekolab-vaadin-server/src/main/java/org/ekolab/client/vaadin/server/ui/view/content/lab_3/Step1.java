package org.ekolab.client.vaadin.server.ui.view.content.lab_3;

import com.vaadin.server.CompositeErrorMessage;
import com.vaadin.server.ErrorMessage;
import com.vaadin.spring.annotation.SpringView;
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
@SpringView
public class Step1 extends HorizontalLayout implements LabWizardStep {
    @Autowired
    private I18N i18N;

    @Autowired
    private ParameterLayout<Lab3Data> firstFormLayout;

    @Autowired
    private ParameterLayout<Lab3Data> secondFormLayout;

    // ----------------------------- Графические компоненты --------------------------------

    @Override
    public void init() {
        LabWizardStep.super.init();
        setSizeFull();
        setMargin(true);
        addComponent(firstFormLayout);
        addComponent(secondFormLayout);
        firstFormLayout.setMargin(true);
        firstFormLayout.setCaption(i18N.get("lab3.step1.power-station-characteristics"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "tppOutput"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "numberOfUnits"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "city"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "steamProductionCapacity"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "numberOfStacks"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "stacksHeight"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "stacksDiameter"));

        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "windSpeed"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "windDirection"));

        secondFormLayout.setMargin(true);
        secondFormLayout.setCaption(i18N.get("lab3.step1.fuel-characteristics"));
        secondFormLayout.addField(FieldUtils.getField(Lab3Data.class, "lowHeatValue"));
        secondFormLayout.addField(FieldUtils.getField(Lab3Data.class, "carbonInFlyAsh"));
        secondFormLayout.addField(FieldUtils.getField(Lab3Data.class, "fuelConsumer"));
        secondFormLayout.addField(FieldUtils.getField(Lab3Data.class, "sulphurContent"));
        secondFormLayout.addField(FieldUtils.getField(Lab3Data.class, "ashContent"));
        secondFormLayout.addField(FieldUtils.getField(Lab3Data.class, "waterContent"));
        secondFormLayout.addField(FieldUtils.getField(Lab3Data.class, "ashRecyclingFactor"));
    }

    @Override
    public ErrorMessage getComponentError() {
        Set<ErrorMessage> messageList = Stream.of(super.getComponentError(),
                firstFormLayout.getComponentError(),  secondFormLayout.getComponentError())
                .filter(Objects::nonNull).collect(Collectors.toSet());
        return messageList.isEmpty() ? null : new CompositeErrorMessage(messageList);
    }
}
