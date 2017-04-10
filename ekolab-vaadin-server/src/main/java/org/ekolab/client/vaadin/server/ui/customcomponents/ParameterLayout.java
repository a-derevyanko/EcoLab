package org.ekolab.client.vaadin.server.ui.customcomponents;

import com.vaadin.data.Binder;
import com.vaadin.data.Converter;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Setter;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.service.Validators;
import org.ekolab.client.vaadin.server.ui.view.api.UIComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.annotation.PrototypeScope;

/**
 * Created by 777Al on 08.04.2017.
 */
@SpringComponent
@PrototypeScope
public class ParameterLayout<BEAN> extends HorizontalLayout implements UIComponent {
    @Autowired
    private Binder<BEAN> dataBinder;

    @Autowired
    private I18N i18N;

    @Autowired
    private StringToIntegerConverter strToInt;

    @Autowired
    private StringToDoubleConverter strToDouble;

    @Autowired
    private Validators validators;

    // ---------------------------- Графические компоненты --------------------
    private final FormLayout formLayout = new FormLayout();
    private final VerticalLayout dimensionLabelsLayout = new VerticalLayout();
    private final VerticalLayout infoButtonsLayout = new VerticalLayout();

    @Override
    public void init() {
        UIComponent.super.init();
        super.addComponent(formLayout);
        super.addComponent(dimensionLabelsLayout);
        super.addComponent(infoButtonsLayout);
    }

    @Override
    public void setCaption(String captionKey) {

    }

    public void addIntegerField(String captionKey, int min, int max, ValueProvider<BEAN, Integer> getter,
                                 Setter<BEAN, Integer> setter) {
        addField(captionKey, strToInt, validators.intValidator(min, max), getter, setter);
    }

    public void addDoubleField(String captionKey, double min, double max, ValueProvider<BEAN, Double> getter,
                                 Setter<BEAN, Double> setter) {
        addField(captionKey, strToDouble, validators.doubleValidator(min, max), getter, setter);
    }

    public <T> void addField(String captionKey, Converter<String, T> converter, Validator<T> validator, ValueProvider<BEAN, T> getter,
                             Setter<BEAN, T> setter) {
        TextField field = new TextField(i18N.get(captionKey));
        dataBinder.forField(field).withConverter(converter).
                withValidator(validator).bind(getter, setter);

        Label dimensionLabel = new Label("km2");
        Button infoButton = new Button(VaadinIcons.QUESTION);
        formLayout.addComponent(field);
        dimensionLabelsLayout.addComponent(dimensionLabel);
        infoButtonsLayout.addComponent(infoButton);
    }

    @Override
    public void addComponent(Component c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addComponent(Component c, int index) {
        throw new UnsupportedOperationException();
    }
}
