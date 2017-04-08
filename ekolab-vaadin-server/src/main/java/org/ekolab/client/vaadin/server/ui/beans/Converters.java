package org.ekolab.client.vaadin.server.ui.beans;

import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.spring.annotation.UIScope;
import org.ekolab.client.vaadin.server.service.I18N;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

/**
 * Created by 777Al on 07.04.2017.
 */
@SpringBootConfiguration
@Lazy
public class Converters {

    @Autowired
    private I18N i18N;

    @Bean
    @UIScope
    public StringToIntegerConverter stringToIntegerConverter() {
        return new StringToIntegerConverter(i18N.get("validator.must-be-number"));
    }

    @Bean
    @UIScope
    public StringToDoubleConverter stringToDoubleConverter() {
        return new StringToDoubleConverter(i18N.get("validator.must-be-double"));
    }
}
