package org.ecolab.client.vaadin.server.ui.customcomponents;

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
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.vaadin.util.ReflectTools;
import org.ecolab.client.vaadin.server.service.api.ParameterCustomizer;
import org.ecolab.client.vaadin.server.service.api.ResourceService;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.common.UIUtils;
import org.ecolab.client.vaadin.server.ui.styles.EcoLabTheme;
import org.ecolab.client.vaadin.server.ui.view.api.UIComponent;
import org.ecolab.client.vaadin.server.ui.windows.ResourceWindow;
import org.ecolab.server.model.content.LabData;
import org.ecolab.server.model.content.LabVariant;
import org.ecolab.server.service.api.content.LabService;
import org.ecolab.server.service.api.content.ValidationService;
import org.springframework.boot.autoconfigure.mustache.MustacheProperties;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
        setStyleName(EcoLabTheme.LAYOUT_LAB);
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
        captionLabel.addStyleName(EcoLabTheme.LABEL_TINY);
        super.addComponent(captionLabel, 0, row);
        setComponentAlignment(captionLabel, Alignment.MIDDLE_LEFT);
    }

    protected void addSign(String fieldName, int row) {
        Label signLabel = new Label(i18N.get(fieldName + "-sign"), ContentMode.HTML);
        signLabel.addStyleName(EcoLabTheme.LABEL_SIGN);
        signLabel.addStyleName(EcoLabTheme.LABEL_TINY);
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
            comboBox.addStyleName(EcoLabTheme.COMBOBOX_TINY);
            comboBox.setWidth(130, Unit.PIXELS);
            component = comboBox;
        } else if (propClass == Boolean.class) {
            RadioButtonGroup<Boolean> yesNoComponent = new RadioButtonGroup<>(null, Arrays.asList(Boolean.FALSE, Boolean.TRUE));
            yesNoComponent.setItemCaptionGenerator(item -> item ? i18N.get("labwizard.yes-value") : i18N.get("labwizard.no-value"));
            yesNoComponent.setStyleName(EcoLabTheme.OPTIONGROUP_HORIZONTAL);
            yesNoComponent.setSizeFull();
            dataBinder.forField(yesNoComponent).bind(propertyField.getName());
            yesNoComponent.setWidth(130, Unit.PIXELS);
            component = yesNoComponent;
        } else if (propClass == String.class || Number.class.isAssignableFrom(propClass)) {
            TextField field = new TextField();
            Converter<String, ?> converter = UIUtils.getStringConverter(propertyField, i18N);

            UIUtils.bindField(propertyField, dataBinder.forField(field).withNullRepresentation(readOnly ? i18N.get("labwizard.unknown-value") : "")
                    .withConverter(converter), dataBinder, validationService, i18N);
            field.setReadOnly(readOnly);
            field.addStyleName(EcoLabTheme.TEXTFIELD_TINY);
            field.setWidth(130, Unit.PIXELS);
            component = field;
        } else if (propClass == LocalDateTime.class) {
            DateTimeField field = new DateTimeField();
            dataBinder.forField(field).bind(propertyField.getName());
            field.setWidth(200, Unit.PIXELS);
            component = field;
        }  else if (propClass == List.class) {
            Type[] type = ((ParameterizedType) propertyField.getGenericType()).getActualTypeArguments();
            if (type[0] == Double.class) {
                ListField<Double> field = new ListField<>(0.0);
                List<String> columns = Arrays.asList(i18N.get(propertyField.getName() + "-columns").split(";"));
                field.createColumns(i18N, UIUtils.getStringConverter(Double.class, i18N), columns);
                dataBinder.forField(field).bind(propertyField.getName());
                field.setWidth(100 * columns.size(), Unit.PIXELS);
                /*PopupView view = new PopupView(null, field);
                view.setHideOnMouseOut(false);
                field.addValueChangeListener(e -> view.setPopupVisible(false));
                Button button = new Button(i18N.get("labwizard.input-values"), event -> view.setPopupVisible(true));*/
                Window view = new Window(i18N.get("labwizard.input-values"), field);
                view.setResizable(false);
                field.addValueChangeListener(e -> UI.getCurrent().removeWindow(view));
                Button button = new Button(i18N.get("labwizard.input-values"), event -> {
                    view.center();
                    UI.getCurrent().addWindow(view);
                });
                button.setStyleName(EcoLabTheme.BUTTON_SMALL);
                component = button;
            }  else {
                throw new IllegalArgumentException("Unknown property class: " + propClass);
            }
        } else {
            throw new IllegalArgumentException("Unknown property class: " + propClass);
        }
        super.addComponent(component, 1, row);
    }


    public void addInfoButton(String fieldName, int row) {
        if (res.isResourceExists(additionsPath, fieldName + MustacheProperties.DEFAULT_SUFFIX)) {
            Button infoButton = new Button(VaadinIcons.QUESTION);
            infoButton.addClickListener(event -> resourceWindow.show(
                    new ResourceWindow.ResourceWindowSettings(i18N.get(fieldName),
                            res.getHtmlData(additionsPath, fieldName + MustacheProperties.DEFAULT_SUFFIX), false)));
            infoButton.addStyleName(EcoLabTheme.BUTTON_TINY);
            super.addComponent(infoButton, 3, row);
        }
    }
}
