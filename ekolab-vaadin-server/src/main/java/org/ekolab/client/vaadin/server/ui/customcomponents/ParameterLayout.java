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
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.client.vaadin.server.ui.view.api.UIComponent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 777Al on 08.04.2017.
 */
public class ParameterLayout<BEAN> extends GridLayout implements UIComponent {
    private final Binder<BEAN> dataBinder;

    private final StringToIntegerConverter strToInt;

    private final StringToDoubleConverter strToDouble;

    private final I18N i18N;

    public ParameterLayout(Binder<BEAN> dataBinder, StringToIntegerConverter strToInt, StringToDoubleConverter strToDouble, I18N i18N) {
        this.dataBinder = dataBinder;
        this.strToInt = strToInt;
        this.strToDouble = strToDouble;
        this.i18N = i18N;
    }

    // ---------------------------- Графические компоненты --------------------
    @Override
    public void init() {
        UIComponent.super.init();
        setColumns(4);
        setSpacing(true);
        setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        setStyleName(EkoLabTheme.LAYOUT_LAB);
    }

    public void addField(Field propertyField) {
        Component field = getComponent(propertyField);

        Label captionLabel = new Label(i18N.get(propertyField.getName()), ContentMode.PREFORMATTED);
        Label dimensionLabel = new Label("km2", ContentMode.PREFORMATTED);
        Button infoButton = new Button(VaadinIcons.QUESTION);
        int lastRow = getRows();
        insertRow(lastRow);
        newLine();
        super.addComponent(captionLabel, 0, lastRow);
        super.addComponent(field, 1, lastRow);
        super.addComponent(dimensionLabel, 2, lastRow);
        super.addComponent(infoButton, 3, lastRow);
    }

    @Override
    public void addComponent(Component c) {
        throw new UnsupportedOperationException();
    }

    private AbstractComponent getComponent(Field propertyField) {
        Class<?> propClass = ReflectTools.convertPrimitiveType(propertyField.getType());
        if (propClass.isEnum()) {
            ComboBox<Enum<?>> comboBox = new ComboBox<Enum<?>>(null, (List) Arrays.asList(propClass.getEnumConstants()));
            comboBox.setItemCaptionGenerator((elem) -> i18N.get(elem.getDeclaringClass().getSimpleName() + '.' + elem.name()));
            comboBox.setTextInputAllowed(false);
            comboBox.setPageLength(15);
            comboBox.setEmptySelectionAllowed(false);
            dataBinder.forField(comboBox).bind(propertyField.getName());
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
            return field;
        }
    }

    @Override
    public CompositeErrorMessage getComponentError() {
        List<ErrorMessage> errorMessages = new ArrayList<>();
        errorMessages.add(super.getComponentError());
        for (int i = 0; i < getRows(); i++) {
            AbstractComponent component = (AbstractComponent) getComponent(1, i); // Поля ввода данных
            errorMessages.add(component.getErrorMessage());
        }
        return errorMessages.isEmpty() ? null : new CompositeErrorMessage(errorMessages);
    }
}
