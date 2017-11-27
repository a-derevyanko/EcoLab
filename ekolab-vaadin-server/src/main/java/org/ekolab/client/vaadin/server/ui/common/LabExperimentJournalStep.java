package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.data.Binder;
import com.vaadin.data.Converter;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.ekolab.client.vaadin.server.service.api.ParameterCustomizer;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.customcomponents.ComponentErrorNotification;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.LabVariant;
import org.ekolab.server.model.content.lab1.Lab1ExperimentLog;
import org.ekolab.server.service.api.content.ValidationService;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

public abstract class LabExperimentJournalStep<T extends LabData<V>, V extends LabVariant> extends VerticalLayout implements LabWizardStep<T, V> {
    protected final Binder<V> experimentLogBinder;

    protected final Binder<T> dataBinder;

    protected final I18N i18N;

    protected final ValidationService validationService;

    protected final ParameterCustomizer parameterCustomizer;

    // ----------------------------- Графические компоненты --------------------------------
    protected final GridLayout firstLayout = new GridLayout(5, 7);
    protected final GridLayout secondLayout = new GridLayout(5, 7);
    protected final HorizontalLayout centerLayout = new HorizontalLayout(firstLayout, secondLayout);
    protected final Label journalLabel = new Label("Experiment journal");

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
        journalLabel.setStyleName(EkoLabTheme.LABEL_BOLD_ITALIC);
        firstLayout.setMargin(true);
        firstLayout.setSpacing(true);
        secondLayout.setMargin(true);
        secondLayout.setSpacing(true);
    }

    protected void addField(AbstractField<?> component, Field field, GridLayout layout, int row) {
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
            Binder.BindingBuilder<V, String> bindingBuilder = experimentLogBinder.forField((TextField)component).withNullRepresentation("");
            Converter<String, ?> converter = UIUtils.getStringConverter(field, i18N);
            if (converter == null) {
                UIUtils.bindField(field, bindingBuilder, experimentLogBinder, validationService, i18N);
            } else {
                bindingBuilder.withConverter(converter).bind(field.getName());
            }
        } else {
            experimentLogBinder.forField(component).bind(field.getName());
        }
    }

    /**
     * Копирование свойств из варианта в данные лабораторной
     * @return возможность перехода на следующий щаг
     */
    @Override
    public boolean onAdvance() {
        for (Field field : Lab1ExperimentLog.class.getDeclaredFields()) {
            ReflectionUtils.makeAccessible(field);
            Object value = ReflectionUtils.getField(field, experimentLogBinder.getBean());
            if (value == null) {
                ComponentErrorNotification.show(i18N.get("labwizard.next-step-unavailable"), i18N.get("lab1.step1.fill-variant"));
                return false;
            }
        }
        BeanUtils.copyProperties(experimentLogBinder.getBean(), dataBinder.getBean());

        return LabWizardStep.super.onAdvance();
    }
}
