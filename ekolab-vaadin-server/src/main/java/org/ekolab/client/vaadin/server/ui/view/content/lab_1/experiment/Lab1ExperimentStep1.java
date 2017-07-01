package org.ekolab.client.vaadin.server.ui.view.content.lab_1.experiment;

import com.vaadin.data.Binder;
import com.vaadin.data.Converter;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.service.ParameterCustomizer;
import org.ekolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ekolab.client.vaadin.server.ui.common.UIUtils;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
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

    @Autowired
    private ParameterCustomizer parameterCustomizer;
    // ----------------------------- Графические компоненты --------------------------------
    private final Binder<Lab1Variant> binder = new Binder<>(Lab1Variant.class);
    private final GridLayout journalLayout = new GridLayout(6, 7);
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
        setComponentAlignment(journalLayout, Alignment.MIDDLE_CENTER);
        //journalLayout.setSizeFull();
        journalLayout.setCaption(i18N.get("lab1.step1.experiment-journal"));
        addField(timeField, FieldUtils.getField(Lab1Variant.class, "time"), 0, 0);
        addField(objectNameField, FieldUtils.getField(Lab1Variant.class, "name"), 0, 1);
        addField(barometricPressureField, FieldUtils.getField(Lab1Variant.class, "barometricPressure"), 0, 2);
        addField(outsideAirTemperatureField, FieldUtils.getField(Lab1Variant.class, "outsideAirTemperature"), 0, 3);
        addField(stacksHeightField, FieldUtils.getField(Lab1Variant.class, "stacksHeight"), 0, 4);
        addField(stacksDiameterField, FieldUtils.getField(Lab1Variant.class, "stacksDiameter"), 0, 5);
        addField(steamProductionCapacityField, FieldUtils.getField(Lab1Variant.class, "steamProductionCapacity"), 0, 6);
        addField(oxygenConcentrationField, FieldUtils.getField(Lab1Variant.class, "oxygenConcentration"), 1, 0);
        addField(oxygenConcentrationPointField, FieldUtils.getField(Lab1Variant.class, "oxygenConcentrationPoint"), 1, 1);
        addField(fuelConsumerField, FieldUtils.getField(Lab1Variant.class, "fuelConsumer"), 1, 2);
        addField(excessPressureField, FieldUtils.getField(Lab1Variant.class, "excessPressure"), 1, 3);
        addField(gasTemperatureField, FieldUtils.getField(Lab1Variant.class, "gasTemperature"), 1, 4);
        addField(stackExitTemperatureField, FieldUtils.getField(Lab1Variant.class, "stackExitTemperature"), 1, 5);
        addField(flueGasNOxConcentrationField, FieldUtils.getField(Lab1Variant.class, "flueGasNOxConcentration"), 1, 6);
    }

    private void addField(AbstractField<?> component, Field field, int column, int row) {
        Label numberLabel = new Label(parameterCustomizer.getParameterPrefix());
        numberLabel.addStyleName(EkoLabTheme.LABEL_TINY);
        Label captionLabel = new Label(i18N.get(field.getName()), ContentMode.HTML);
        captionLabel.addStyleName(EkoLabTheme.LABEL_TINY);
        journalLayout.addComponent(numberLabel, column * 3, row);
        journalLayout.addComponent(captionLabel, column * 3 + 1, row);
        journalLayout.addComponent(component, column * 3 + 2, row);
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
