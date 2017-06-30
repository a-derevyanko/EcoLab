package org.ekolab.client.vaadin.server.ui.view.content.lab_1.experiment;

import com.vaadin.data.Binder;
import com.vaadin.data.Converter;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ekolab.client.vaadin.server.ui.common.UIUtils;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.util.FieldUtils;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringComponent
@ViewScope
public class Lab1ExperimentStep1 extends HorizontalLayout implements LabWizardStep {
    @Autowired
    private I18N i18N;

    // ----------------------------- Графические компоненты --------------------------------
    private final Binder<Lab1Variant> binder = new Binder<>(Lab1Variant.class);
    private final GridLayout journalLayout = new GridLayout(10, 6);
    private final DateTimeField timeField = new DateTimeField();
    private final TextField objectNameField = new TextField();
    private final TextField barometricPressureField = new TextField();
    private final TextField outsideAirTemperatureField = new TextField();
    private final TextField stacksHeightField = new TextField();
    private final TextField stacksDiameterField = new TextField();
    private final TextField steamProductionCapacityField = new TextField();
    private final TextField oxygenConcentrationField = new TextField();
    private final TextField oxygenConcentrationPointField = new TextField();
    private final TextField fuelConsumerField = new TextField();
    private final TextField excessPressureField = new TextField();
    private final TextField gasTemperatureField = new TextField();
    private final TextField stackExitTemperatureField = new TextField();
    private final TextField flueGasNOxConcentrationField = new TextField();

    @Override
    public void init() throws IOException {
        LabWizardStep.super.init();
        setSizeFull();
        setMargin(true);
        addComponent(journalLayout);
        journalLayout.setSizeFull();
        journalLayout.setCaption(i18N.get("lab1.step1.experiment-journal"));
        addField(objectNameField, FieldUtils.getField(Lab1Variant.class, "name"), 1);
        addField(barometricPressureField, FieldUtils.getField(Lab1Variant.class, "barometricPressure"), 2);
        addField(outsideAirTemperatureField, FieldUtils.getField(Lab1Variant.class, "outsideAirTemperature"), 3);
        addField(stacksHeightField, FieldUtils.getField(Lab1Variant.class, "stacksHeight"), 4);
        addField(stacksDiameterField, FieldUtils.getField(Lab1Variant.class, "stacksDiameter"), 5);
        addField(steamProductionCapacityField, FieldUtils.getField(Lab1Variant.class, "steamProductionCapacity"), 6);
        addField(oxygenConcentrationField, FieldUtils.getField(Lab1Variant.class, "oxygenConcentration"), 7);
        addField(oxygenConcentrationPointField, FieldUtils.getField(Lab1Variant.class, "oxygenConcentrationPoint"), 8);
        addField(fuelConsumerField, FieldUtils.getField(Lab1Variant.class, "fuelConsumer"), 9);
        addField(excessPressureField, FieldUtils.getField(Lab1Variant.class, "excessPressure"), 10);
        addField(gasTemperatureField, FieldUtils.getField(Lab1Variant.class, "gasTemperature"), 11);
        addField(stackExitTemperatureField, FieldUtils.getField(Lab1Variant.class, "stackExitTemperature"), 12);
        addField(flueGasNOxConcentrationField, FieldUtils.getField(Lab1Variant.class, "flueGasNOxConcentration"), 13);
    }

    private void addField(AbstractField<?> component, Field field, int row) {
        journalLayout.addComponent(new Label(row + "."), 0, row);
        journalLayout.addComponent(new Label(i18N.get(field.getName()), ContentMode.HTML), 1, row);
        journalLayout.addComponent(component, 2, row);
        if (component instanceof TextField) {
            Converter<String, ?> converter = UIUtils.getStringConverter(field, i18N);
            if (converter != null) {
                binder.forField((TextField)component).withConverter(converter).bind(field.getName());
                return;
            }
        }
        binder.forField(component).bind(field.getName());
    }
}
