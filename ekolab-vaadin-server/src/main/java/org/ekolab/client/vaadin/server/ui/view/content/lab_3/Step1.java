package org.ekolab.client.vaadin.server.ui.view.content.lab_3;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.server.AbstractErrorMessage;
import com.vaadin.server.CompositeErrorMessage;
import com.vaadin.server.ErrorMessage;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.HorizontalLayout;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.common.AutoSavableLabWizardStep;
import org.ekolab.client.vaadin.server.ui.customcomponents.ParameterLayout;
import org.ekolab.server.model.Lab3Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.util.FieldUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringView
public class Step1 extends HorizontalLayout implements AutoSavableLabWizardStep {
    @Autowired
    private Binder<Lab3Data> lab3DataBinder;

    @Autowired
    private I18N i18N;

    @Autowired
    private ParameterLayout<Lab3Data> firstFormLayout;

    @Autowired
    private ParameterLayout<Lab3Data> secondFormLayout;

    // ----------------------------- Графические компоненты --------------------------------

    @Override
    public void init() {
        AutoSavableLabWizardStep.super.init();
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
    public void saveData() {
        List<ErrorMessage> messageList = new ArrayList<>();
        messageList.add(firstFormLayout.getComponentError());
        messageList.add(secondFormLayout.getComponentError());
        // todo ошибка может возникунть при сохранении messageList.add(secondFormLayout.getComponentError()); lab3DataBinder.writeBeanIfValid();
        try {
            lab3DataBinder.writeBean(null);
        } catch (ValidationException exception) {
            messageList.add(AbstractErrorMessage.getErrorMessageForException(exception));
        }
        String error = new CompositeErrorMessage(messageList).getFormattedHtmlMessage();
    }
}
