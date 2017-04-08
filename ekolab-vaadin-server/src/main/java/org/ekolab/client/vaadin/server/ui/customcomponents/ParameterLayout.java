package org.ekolab.client.vaadin.server.ui.customcomponents;

import com.vaadin.data.Binder;
import com.vaadin.data.Converter;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.server.Setter;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.ekolab.client.vaadin.server.service.I18N;
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

    // ---------------------------- Графические компоненты --------------------
    private final FormLayout formLayout = new FormLayout();
    private final VerticalLayout dimensionLabelsLayout = new VerticalLayout();
    private final VerticalLayout infoLabelsLayout = new VerticalLayout();

    @Override
    public void init() {
        UIComponent.super.init();
    }

    @Override
    public void setCaption(String captionKey) {

    }

    public void addIntegerField(int min, int max, ValueProvider<BEAN, Integer> getter,
                                 Setter<BEAN, Integer> setter) {

    }

    public <T> void addField(TextField field, Converter<String, T> converter, Validator<T> validator, ValueProvider<BEAN, T> getter,
                             Setter<BEAN, T> setter) {
        dataBinder.forField(field).withConverter(converter).
                withValidator(validator).bind(getter, setter);
    }

    @Override
    public void addComponent(Component c) {
        //throw new UnsupportedOperationException();
    }

    @Override
    public void addComponent(Component c, int index) {
        //throw new UnsupportedOperationException();
    }
}
