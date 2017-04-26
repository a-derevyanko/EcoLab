package org.ekolab.client.vaadin.server.ui.customcomponents;

import com.vaadin.data.Binder;
import com.vaadin.data.Converter;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.CompositeErrorMessage;
import com.vaadin.server.ErrorMessage;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.util.ReflectTools;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.service.ResourceService;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.client.vaadin.server.ui.view.api.UIComponent;
import org.ekolab.server.service.api.content.LabService;
import org.springframework.boot.autoconfigure.mustache.MustacheProperties;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.ekolab.client.vaadin.server.ui.common.ResourceWindow.show;

/**
 * Created by 777Al on 08.04.2017.
 */
public class ParameterLayout<BEAN> extends GridLayout implements UIComponent {
    private final String parametersPath;

    private final Binder<BEAN> dataBinder;

    private final LabService labService;

    private final StringToIntegerConverter strToInt;

    private final StringToDoubleConverter strToDouble;

    private final I18N i18N;

    private final ResourceService res;

    public ParameterLayout(String parametersPath, Binder<BEAN> dataBinder, LabService labService, StringToIntegerConverter strToInt, StringToDoubleConverter strToDouble, I18N i18N, ResourceService res) {
        this.parametersPath = parametersPath;
        this.dataBinder = dataBinder;
        this.labService = labService;
        this.strToInt = strToInt;
        this.strToDouble = strToDouble;
        this.i18N = i18N;
        this.res = res;
    }

    // ---------------------------- Графические компоненты --------------------
    @Override
    public void init() throws Exception {
        UIComponent.super.init();
        setColumns(4);
        setMargin(true);
        setSpacing(false);
        setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        setStyleName(EkoLabTheme.LAYOUT_LAB);
    }

    public void addField(Field propertyField) {
        Component field = getComponent(propertyField);

        Label captionLabel = new Label(getFieldCaption(propertyField.getName()), ContentMode.HTML);
        Label signLabel = new Label(getFieldSign(propertyField.getName()), ContentMode.HTML);
        signLabel.addStyleName(EkoLabTheme.LABEL_SIGN);
        String fieldName = propertyField.getName();
        Button infoButton = new Button(VaadinIcons.QUESTION);
        infoButton.addClickListener(event -> show(i18N.get(fieldName), res.getHtmlData(parametersPath, fieldName + MustacheProperties.DEFAULT_SUFFIX)));
        int lastRow = getRows() - 1;
        super.addComponent(captionLabel, 0, lastRow);
        super.addComponent(field, 1, lastRow);
        super.addComponent(signLabel, 2, lastRow);
        super.addComponent(infoButton, 3, lastRow);
        insertRow(getRows());
    }

    @Override
    public void addComponent(Component c) {
        throw new UnsupportedOperationException();
    }

    private String getFieldCaption(String fieldName) {
        String fieldCaption = i18N.get(fieldName);
        String fieldDimension = i18N.get(fieldName + "-dimension");
        return fieldDimension.isEmpty() ? fieldCaption : fieldCaption + " (" + fieldDimension + ')';
    }

    private String getFieldSign(String fieldName) {
        return i18N.get(fieldName + "-sign");
    }

    private AbstractComponent getComponent(Field propertyField) {
        Class<?> propClass = ReflectTools.convertPrimitiveType(propertyField.getType());
        boolean readOnly = labService.isFieldCalculated(propertyField);
        if (propClass.isEnum()) {
            ComboBox<Enum<?>> comboBox = new ComboBox<>(null, Arrays.asList((Enum<?>[]) propClass.getEnumConstants()));
            comboBox.setItemCaptionGenerator((elem) -> i18N.get(elem.getDeclaringClass().getSimpleName() + '.' + elem.name()));
            comboBox.setTextInputAllowed(false);
            comboBox.setPageLength(15);
            comboBox.setEmptySelectionAllowed(false);
            dataBinder.forField(comboBox).bind(propertyField.getName());
            comboBox.setReadOnly(readOnly);
            return comboBox;
        } else {
            TextField field = new TextField();
            Converter<String, ?> converter;
            if (propClass == Integer.class) {
                converter = strToInt;
            } else if (propClass == Double.class) {
                converter = strToDouble;
            } else {
                throw new IllegalArgumentException("Unknown field type");
            }

            dataBinder.forField(field).withConverter(converter).bind(propertyField.getName());
            field.setReadOnly(readOnly);
            return field;
        }
    }

    @Override
    public CompositeErrorMessage getComponentError() {
        List<ErrorMessage> errorMessages = new ArrayList<>(0);
        if (super.getComponentError() != null) {
            errorMessages.add(super.getComponentError());
        }
        for (int i = 0; i < getRows() - 1; i++) {
            AbstractComponent component = (AbstractComponent) getComponent(1, i); // Поля ввода данных
            if (component.getComponentError() != null) {
                errorMessages.add(component.getComponentError());
            }
        }
        return errorMessages.isEmpty() ? null : new CompositeErrorMessage(errorMessages);
    }
}
