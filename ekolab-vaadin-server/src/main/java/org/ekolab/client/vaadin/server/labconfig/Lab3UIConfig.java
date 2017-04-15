package org.ekolab.client.vaadin.server.labconfig;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.spring.annotation.ViewScope;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.customcomponents.ParameterLayout;
import org.ekolab.server.model.Lab3Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.vaadin.spring.annotation.PrototypeScope;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringBootConfiguration
public class Lab3UIConfig {
    @Bean
    @ViewScope
    public Binder<Lab3Data> lab3Binder() {
        return new Binder<>(Lab3Data.class);
    }

    @Bean
    @PrototypeScope
    public ParameterLayout<Lab3Data> parameterLayout(Binder<Lab3Data> binder, StringToIntegerConverter stringToIntegerConverter,
                                                     StringToDoubleConverter stringToDoubleConverter, I18N i18N) {
        return new ParameterLayout<>(binder, stringToIntegerConverter, stringToDoubleConverter, i18N);
    }
}
