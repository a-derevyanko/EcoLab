package org.ecolab.client.vaadin.server.ui.common;

import com.vaadin.data.Binder;
import com.vaadin.data.Converter;
import com.vaadin.data.HasValue;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.data.ValueProvider;
import com.vaadin.server.Setter;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.util.ReflectTools;
import org.ecolab.client.vaadin.server.service.api.ParameterCustomizer;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.customcomponents.ComponentErrorNotification;
import org.ecolab.client.vaadin.server.ui.styles.EcoLabTheme;
import org.ecolab.server.model.content.LabData;
import org.ecolab.server.model.content.LabVariant;
import org.ecolab.server.service.api.content.ValidationService;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public abstract class LabExperimentJournalStep<T extends LabData<V>, V extends LabVariant> extends VerticalLayout implements LabWizardStep<T, V> {
    protected final Binder<V> experimentLogBinder;

    protected final Binder<T> dataBinder;

    protected final I18N i18N;

    protected final ValidationService validationService;

    protected final ParameterCustomizer parameterCustomizer;

    protected float fieldWidth = 175.0F;
    // ----------------------------- Графические компоненты --------------------------------
    protected final Label objectLabel = new Label("Object");
    protected final GridLayout firstLayout = new GridLayout(5, 2);
    protected final HorizontalLayout centerLayout = new HorizontalLayout(firstLayout);
    protected final Label journalLabel = new Label("Experiment journal");

    protected final Binder<String> averageValuesBinder = new Binder<>();

    public LabExperimentJournalStep(Binder<V> experimentLogBinder, Binder<T> dataBinder, I18N i18N, ValidationService validationService, ParameterCustomizer parameterCustomizer) {
        this.experimentLogBinder = experimentLogBinder;
        this.dataBinder = dataBinder;
        this.i18N = i18N;
        this.validationService = validationService;
        this.parameterCustomizer = parameterCustomizer;
    }

    @Override
    public void init() {
        LabWizardStep.super.init();
        setSizeFull();
        setMargin(true);
        addComponent(journalLabel);
        addComponent(centerLayout);
        setComponentAlignment(journalLabel, Alignment.TOP_CENTER);
        setComponentAlignment(centerLayout, Alignment.MIDDLE_CENTER);
        setExpandRatio(centerLayout, 1.0f);
        journalLabel.setValue(i18N.get("lab.step-experiment-journal.title"));
        journalLabel.setStyleName(EcoLabTheme.LABEL_BOLD_ITALIC);
        firstLayout.setMargin(true);
        firstLayout.setSpacing(true);
        firstLayout.addComponent(objectLabel, 0, 0, 4, 0);
        firstLayout.setComponentAlignment(objectLabel, Alignment.TOP_CENTER);
        objectLabel.setValue(i18N.get("lab1.step1.object-title"));
        objectLabel.setStyleName(EcoLabTheme.LABEL_BOLD_ITALIC);
    }

    /**
     * Добавляет три поля для измерений и поле со средним значением
     * @param field поле класса журнала наблюдений
     */
    protected void addTextFieldWithAverageFields(GridLayout layout, Field field) {
        var component = new TextField();
        component.setWidth(100.0F, Unit.PIXELS);
        component.setEnabled(false);
        List<Component> components = new ArrayList<>(createFieldComponents(component, field));

        List<TextField> measurementComponents = new ArrayList<>(3);

        final var converter = UIUtils.getStringConverter(Double.class, i18N);
        for (var i = 1; i < 4; i++) {
            var iMeasurementTextField = new TextField();
            iMeasurementTextField.setPlaceholder(i18N.get("lab.step-experiment-journal.measurement", i));
            iMeasurementTextField.setWidth(50.0F, Unit.PIXELS);

            averageValuesBinder.forField(iMeasurementTextField).withConverter(converter).bind((ValueProvider<String, Double>) s -> null, (Setter<String, Double>) (s, o) -> { });
            measurementComponents.add(iMeasurementTextField);
        }
        components.addAll(2, measurementComponents);

        var context = new ValueContext();
        HasValue.ValueChangeListener<String> listener = event -> {
            if (averageValuesBinder.isValid()) {
                var value = measurementComponents.stream().filter(Objects::nonNull)
                        .map(i -> {
                            var result = converter.convertToModel(i.getValue(), context);
                                    return result.isError() ? null : result.getOrThrow(IllegalStateException::new);
                                }
                        ).filter(Objects::nonNull).mapToDouble(i -> i).average().orElse(0.0);
                var propClass = ReflectTools.convertPrimitiveType(field.getType());
                if (propClass == Integer.class) {
                    component.setValue(String.valueOf(Math.round(value)));
                } else if (propClass == Double.class) {
                    component.setValue(NumberFormat.getInstance().format(value));
                } else {
                    throw new IllegalArgumentException("Unsupported field type: " + propClass);
                }
            }
        };
        measurementComponents.forEach(x -> x.addValueChangeListener(listener));

        addComponentsToGridRow(layout, components);
    }

    protected void addField(Field field) {
        AbstractField<?> fieldComponent;
        var propClass = ReflectTools.convertPrimitiveType(field.getType());
        if (propClass == Date.class || propClass == LocalDateTime.class) {
            fieldComponent = new DateTimeField();
        } else {
            fieldComponent = new TextField();
        }
        fieldComponent.setWidth(fieldWidth, Unit.PIXELS);
        addComponentsToGridRow(firstLayout, createFieldComponents(fieldComponent, field));
    }

    private List<Component> createFieldComponents(AbstractField<?> fieldComponent, Field field) {
        var numberLabel = new Label(parameterCustomizer.getParameterPrefix());
        numberLabel.addStyleName(EcoLabTheme.LABEL_TINY);
        var captionLabel = new Label(i18N.get(field.getName()), ContentMode.HTML);
        captionLabel.addStyleName(EcoLabTheme.LABEL_TINY);
        var dimensionLabel = new Label(i18N.get(field.getName() + "-dimension"), ContentMode.HTML);
        dimensionLabel.addStyleName(EcoLabTheme.LABEL_TINY);
        var signLabel = new Label(i18N.get(field.getName() + "-sign"), ContentMode.HTML);
        signLabel.addStyleName(EcoLabTheme.LABEL_TINY);
        if (fieldComponent instanceof TextField) {
            var bindingBuilder = experimentLogBinder.forField((TextField)fieldComponent).withNullRepresentation("");
            var converter = UIUtils.getStringConverter(field, i18N);
            if (converter == null) {
                UIUtils.bindField(field, bindingBuilder, experimentLogBinder, validationService, i18N);
            } else {
                bindingBuilder.withConverter(converter).bind(field.getName());
            }
        } else {
            experimentLogBinder.forField(fieldComponent).bind(field.getName());
        }

        return Arrays.asList(numberLabel, captionLabel, fieldComponent, dimensionLabel, signLabel);
    }

    private void addComponentsToGridRow(GridLayout layout, List<Component> components) {
        var lastRow = layout.getRows() - 1;
        IntStream.range(0, components.size()).forEach(i -> layout.addComponent(components.get(i), i, lastRow));
        layout.insertRow(layout.getRows());
    }

    /**
     * Копирование свойств из варианта в данные лабораторной
     * @return возможность перехода на следующий щаг
     */
    @Override
    public boolean onAdvance() {
        if (!UIUtils.isModelFull(experimentLogBinder.getBean())) {
            ComponentErrorNotification.show(i18N.get("labwizard.next-step-unavailable"), i18N.get("lab1.step1.fill-variant"));
            return false;
        }
        BeanUtils.copyProperties(experimentLogBinder.getBean(), dataBinder.getBean());

        return LabWizardStep.super.onAdvance();
    }
}
