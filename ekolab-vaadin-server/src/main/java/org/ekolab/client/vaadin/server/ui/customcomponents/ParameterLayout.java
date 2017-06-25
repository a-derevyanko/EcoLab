package org.ekolab.client.vaadin.server.ui.customcomponents;

import com.vaadin.data.Binder;
import com.vaadin.data.Converter;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.util.ReflectTools;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.service.ParameterCustomizer;
import org.ekolab.client.vaadin.server.service.ResourceService;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.client.vaadin.server.ui.view.api.UIComponent;
import org.ekolab.server.model.content.LabData;
import org.ekolab.server.service.api.content.LabService;
import org.springframework.boot.autoconfigure.mustache.MustacheProperties;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.ekolab.client.vaadin.server.ui.common.ResourceWindow.show;

/**
 * Created by 777Al on 08.04.2017.
 */
public class ParameterLayout<BEAN extends LabData> extends GridLayout implements UIComponent {
    protected final String parametersPath;

    protected final String additionsPath;

    protected final Binder<BEAN> dataBinder;

    protected final LabService labService;

    protected final I18N i18N;

    protected final ResourceService res;

    protected final ParameterCustomizer parameterCustomizer;

    public ParameterLayout(String parametersPath, Binder<BEAN> dataBinder, LabService labService, I18N i18N,
                           ResourceService res, ParameterCustomizer parameterCustomizer) {
        this.parametersPath = parametersPath;
        this.additionsPath = parametersPath + "additions/";
        this.dataBinder = dataBinder;
        this.labService = labService;
        this.i18N = i18N;
        this.res = res;
        this.parameterCustomizer = parameterCustomizer;
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

    private void addCaption(String fieldName, int row) {
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
        if (propClass.isEnum()) {
            ComboBox<Enum<?>> comboBox = new ComboBox<>(null, Arrays.asList((Enum<?>[]) propClass.getEnumConstants()));
            comboBox.setItemCaptionGenerator((elem) -> i18N.get(elem.getDeclaringClass().getSimpleName() + '.' + elem.name()));
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
            dataBinder.forField(yesNoComponent).bind(propertyField.getName());
            component = yesNoComponent;
        } else {
            TextField field = new TextField();
            String validatorPrefix = i18N.get("validator.value-of-field") + " '"
                    + i18N.get(propertyField.getName()) + "' ";
            Converter<String, ?> converter;
            if (propClass == Integer.class) {
                converter = new StringToIntegerConverter(validatorPrefix + i18N.get("validator.must-be-number"));
            } else if (propClass == Double.class) {
                converter = new StringToDoubleConverter(validatorPrefix + i18N.get("validator.must-be-double"));
            } else {
                throw new IllegalArgumentException("Unknown field type");
            }

            bindField(propertyField, dataBinder.forField(field).withNullRepresentation(readOnly ? i18N.get("labwizard.unknown-value") : "")
                    .withConverter(converter));
            field.setReadOnly(readOnly);
            field.addStyleName(EkoLabTheme.TEXTFIELD_TINY);
            component = field;
        }
        super.addComponent(component, 1, row);
        component.setWidth(130, Unit.PIXELS);
    }

    protected void bindField(Field propertyField, Binder.BindingBuilder<?, ?> builder) {
        builder.bind(propertyField.getName());
    }

    private void addInfoButton(String fieldName, int row) {
        if (res.isResourceExists(additionsPath, fieldName + MustacheProperties.DEFAULT_SUFFIX)) {
            Button infoButton = new Button(VaadinIcons.QUESTION);
            infoButton.addClickListener(event -> show(i18N.get(fieldName), res.getHtmlData(additionsPath, fieldName + MustacheProperties.DEFAULT_SUFFIX)));
            infoButton.addStyleName(EkoLabTheme.BUTTON_TINY);
            super.addComponent(infoButton, 3, row);
        }
    }
}
