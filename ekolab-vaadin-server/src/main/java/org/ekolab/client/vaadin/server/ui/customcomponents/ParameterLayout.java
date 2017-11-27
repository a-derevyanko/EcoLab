package org.ekolab.client.vaadin.server.ui.customcomponents;

import com.vaadin.data.Binder;
import com.vaadin.data.Converter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TextField;
import com.vaadin.util.ReflectTools;
import org.ekolab.client.vaadin.server.service.api.ParameterCustomizer;
import org.ekolab.client.vaadin.server.service.api.ResourceService;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.UIUtils;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.client.vaadin.server.ui.view.api.UIComponent;
import org.ekolab.client.vaadin.server.ui.windows.ResourceWindow;
import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.LabVariant;
import org.ekolab.server.service.api.content.LabService;
import org.ekolab.server.service.api.content.ValidationService;
import org.springframework.boot.autoconfigure.mustache.MustacheProperties;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Created by 777Al on 08.04.2017.
 */
public class ParameterLayout<BEAN extends LabData<V>, V extends LabVariant> extends GridLayout implements UIComponent {
    protected final String parametersPath;

    protected final String additionsPath;

    protected final Binder<BEAN> dataBinder;

    protected final LabService<BEAN, V> labService;

    protected final I18N i18N;

    protected final ResourceService res;

    protected final ParameterCustomizer parameterCustomizer;

    protected final ValidationService validationService;

    protected final ResourceWindow resourceWindow;

    public ParameterLayout(String parametersPath, Binder<BEAN> dataBinder, LabService<BEAN, V> labService, I18N i18N,
                           ResourceService res, ParameterCustomizer parameterCustomizer, ValidationService validationService, ResourceWindow resourceWindow) {
        this.parametersPath = parametersPath;
        this.additionsPath = parametersPath + "additions/";
        this.dataBinder = dataBinder;
        this.labService = labService;
        this.i18N = i18N;
        this.res = res;
        this.parameterCustomizer = parameterCustomizer;
        this.validationService = validationService;
        this.resourceWindow = resourceWindow;
    }

    @Override
    public void init() throws Exception {
        UIComponent.super.init();
        setColumns(4);
        setMargin(true);
        setSpacing(false);
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setStyleName(EkoLabTheme.LAYOUT_LAB);
    }

    public void addField(Field propertyField) {
        int lastRow = getRows() - 1;

        addCaption(propertyField.getName(), lastRow);
        addComponent(propertyField, lastRow);
        addSign(propertyField.getName(), lastRow);
        addInfoButton(propertyField.getName(), lastRow);
        insertRow(getRows());
    }

    @Override
    public void addComponent(Component c) {
        throw new UnsupportedOperationException();
    }

    public void addCaption(String fieldName, int row) {
        String fieldCaption = parameterCustomizer.getParameterPrefix() + i18N.get(fieldName);
        String fieldDimension = i18N.get(fieldName + "-dimension");
        Label captionLabel = new Label(fieldDimension.isEmpty() ? fieldCaption : fieldCaption + " (" + fieldDimension + ')', ContentMode.HTML);
        captionLabel.addStyleName(EkoLabTheme.LABEL_TINY);
        super.addComponent(captionLabel, 0, row);
        setComponentAlignment(captionLabel, Alignment.MIDDLE_LEFT);
    }

    protected void addSign(String fieldName, int row) {
        Label signLabel = new Label(i18N.get(fieldName + "-sign"), ContentMode.HTML);
        signLabel.addStyleName(EkoLabTheme.LABEL_SIGN);
        signLabel.addStyleName(EkoLabTheme.LABEL_TINY);
        super.addComponent(signLabel, 2, row);
    }

    protected void addComponent(Field propertyField, int row) {
        Class<?> propClass = ReflectTools.convertPrimitiveType(propertyField.getType());
        boolean readOnly = labService.isFieldCalculated(propertyField);
        AbstractComponent component;
        if (Enum.class.isAssignableFrom(propClass)) {
            ComboBox<Enum<?>> comboBox = new ComboBox<>(null, Arrays.asList((Enum<?>[]) propClass.getEnumConstants()));
            comboBox.setItemCaptionGenerator(i18N::get);
            comboBox.setTextInputAllowed(false);
            comboBox.setPageLength(15);
            comboBox.setEmptySelectionAllowed(false);
            dataBinder.forField(comboBox).bind(propertyField.getName());
            comboBox.setReadOnly(readOnly);
            comboBox.addStyleName(EkoLabTheme.COMBOBOX_TINY);
            component = comboBox;
        } else if (propClass == Boolean.class) {
            RadioButtonGroup<Boolean> yesNoComponent = new RadioButtonGroup<>(null, Arrays.asList(Boolean.FALSE, Boolean.TRUE));
            yesNoComponent.setItemCaptionGenerator(item -> item ? i18N.get("labwizard.yes-value") : i18N.get("labwizard.no-value"));
            yesNoComponent.setStyleName(EkoLabTheme.OPTIONGROUP_HORIZONTAL);
            yesNoComponent.setSizeFull();
            dataBinder.forField(yesNoComponent).bind(propertyField.getName());
            component = yesNoComponent;
        } else if (propClass == String.class || Number.class.isAssignableFrom(propClass)) {
            TextField field = new TextField();
            Converter<String, ?> converter = UIUtils.getStringConverter(propertyField, i18N);

            UIUtils.bindField(propertyField, dataBinder.forField(field).withNullRepresentation(readOnly ? i18N.get("labwizard.unknown-value") : "")
                    .withConverter(converter), dataBinder, validationService, i18N);
            field.setReadOnly(readOnly);
            field.addStyleName(EkoLabTheme.TEXTFIELD_TINY);
            component = field;
        } else if (propClass == LocalDateTime.class) {
            DateTimeField field = new DateTimeField();
            dataBinder.forField(field).bind(propertyField.getName());
            component = field;
        } else {
            throw new IllegalArgumentException("Unknown property class: " + propClass);
        }
        super.addComponent(component, 1, row);
        component.setWidth(130, Unit.PIXELS);
    }


    public void addInfoButton(String fieldName, int row) {
        if (res.isResourceExists(additionsPath, fieldName + MustacheProperties.DEFAULT_SUFFIX)) {
            Button infoButton = new Button(VaadinIcons.QUESTION);
            infoButton.addClickListener(event -> resourceWindow.show(
                    new ResourceWindow.ResourceWindowSettings(i18N.get(fieldName),
                            res.getHtmlData(additionsPath, fieldName + MustacheProperties.DEFAULT_SUFFIX), false)));
            infoButton.addStyleName(EkoLabTheme.BUTTON_TINY);
            super.addComponent(infoButton, 3, row);
        }
    }
}
