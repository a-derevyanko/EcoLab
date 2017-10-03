package org.ekolab.client.vaadin.server.ui.view.content.lab_1.random;

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
import com.vaadin.ui.VerticalLayout;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.service.api.ParameterCustomizer;
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
public class Lab1RandomStep1 extends VerticalLayout implements LabWizardStep {
    @Autowired
    private Binder<Lab1Variant> binder;

    @Autowired
    private I18N i18N;

    @Autowired
    private ParameterCustomizer parameterCustomizer;

    // ----------------------------- Графические компоненты --------------------------------
    private final GridLayout firstLayout = new GridLayout(5, 7);
    private final GridLayout secondLayout = new GridLayout(5, 7);
    private final HorizontalLayout centerLayout = new HorizontalLayout(firstLayout, secondLayout);
    private final Label journalLabel = new Label("Experiment journal");
    private final DateTimeField timeField = new DateTimeField();
    private final TextField objectNameField = new TextField();
    private final TextField outsideAirTemperatureField = new TextField();
    private final TextField stacksHeightField = new TextField();
    private final TextField stacksDiameterField = new TextField();
    private final TextField steamProductionCapacityField = new TextField();
    private final TextField oxygenConcentrationPointField = new TextField();
    private final TextField fuelConsumerField = new TextField();
    private final TextField stackExitTemperatureField = new TextField();
    private final TextField flueGasNOxConcentrationField = new TextField();

    @Override
    public void init() throws IOException {
        LabWizardStep.super.init();
        setSizeFull();
        setMargin(true);
        addComponent(journalLabel);
        addComponent(centerLayout);
        setComponentAlignment(journalLabel, Alignment.TOP_CENTER);
        setComponentAlignment(centerLayout, Alignment.MIDDLE_CENTER);
        setExpandRatio(centerLayout, 1.0f);
        journalLabel.setValue(i18N.get("lab1.step1.experiment-journal"));
        journalLabel.setStyleName(EkoLabTheme.LABEL_BOLD_ITALIC);
        firstLayout.setMargin(true);
        firstLayout.setSpacing(true);
        secondLayout.setMargin(true);
        secondLayout.setSpacing(true);
        addField(timeField, FieldUtils.getField(Lab1Variant.class, "time"), firstLayout, 0);
        addField(objectNameField, FieldUtils.getField(Lab1Variant.class, "name"), firstLayout, 1);
        addField(outsideAirTemperatureField, FieldUtils.getField(Lab1Variant.class, "outsideAirTemperature"), firstLayout, 2);
        addField(stacksHeightField, FieldUtils.getField(Lab1Variant.class, "stacksHeight"), firstLayout, 3);
        addField(stacksDiameterField, FieldUtils.getField(Lab1Variant.class, "stacksDiameter"), firstLayout, 4);
        addField(steamProductionCapacityField, FieldUtils.getField(Lab1Variant.class, "steamProductionCapacity"), secondLayout, 0);
        addField(oxygenConcentrationPointField, FieldUtils.getField(Lab1Variant.class, "oxygenConcentrationPoint"), secondLayout, 1);
        addField(fuelConsumerField, FieldUtils.getField(Lab1Variant.class, "fuelConsumerNormalized"), secondLayout, 2);
        addField(stackExitTemperatureField, FieldUtils.getField(Lab1Variant.class, "stackExitTemperature"), secondLayout, 3);
        addField(flueGasNOxConcentrationField, FieldUtils.getField(Lab1Variant.class, "flueGasNOxConcentration"), secondLayout, 4);
    }

    private void addField(AbstractField<?> component, Field field, GridLayout layout, int row) {
        Label numberLabel = new Label(parameterCustomizer.getParameterPrefix());
        numberLabel.addStyleName(EkoLabTheme.LABEL_TINY);
        Label captionLabel = new Label(i18N.get(field.getName()), ContentMode.HTML);
        captionLabel.addStyleName(EkoLabTheme.LABEL_TINY);
        Label dimensionLabel = new Label(i18N.get(field.getName() + "-dimension"), ContentMode.HTML);
        dimensionLabel.addStyleName(EkoLabTheme.LABEL_TINY);
        Label signLabel = new Label(i18N.get(field.getName() + "-sign"), ContentMode.HTML);
        signLabel.addStyleName(EkoLabTheme.LABEL_TINY);
        layout.addComponent(numberLabel, 0, row);
        layout.addComponent(captionLabel, 1, row);
        layout.addComponent(component, 2, row);
        layout.addComponent(dimensionLabel, 3, row);
        layout.addComponent(signLabel, 4, row);
        component.setWidth(250.0F, Unit.PIXELS);
        if (component instanceof TextField) {
            Binder.BindingBuilder<Lab1Variant, String> bindingBuilder = binder.forField((TextField)component).withNullRepresentation("");
            Converter<String, ?> converter = UIUtils.getStringConverter(field, i18N);
            if (converter != null) {
                bindingBuilder.withConverter(converter).bind(field.getName());
                return;
            }
        }
        binder.forField(component).bind(field.getName());
    }
}
