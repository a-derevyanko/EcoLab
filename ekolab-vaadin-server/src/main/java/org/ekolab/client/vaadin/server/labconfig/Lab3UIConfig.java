package org.ekolab.client.vaadin.server.labconfig;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.spring.annotation.ViewScope;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.service.ResourceService;
import org.ekolab.client.vaadin.server.ui.customcomponents.ParameterLayout;
import org.ekolab.server.model.content.lab3.Lab3Data;
import org.ekolab.server.service.api.content.lab3.Lab3Service;
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
    public ParameterLayout<Lab3Data> parameterLayout(Binder<Lab3Data> binder,
                                                     Lab3Service labService,
                                                     StringToIntegerConverter stringToIntegerConverter,
                                                     StringToDoubleConverter stringToDoubleConverter,
                                                     I18N i18N, ResourceService resourceService) {
        return new ParameterLayout<>("content/lab3/additions/", binder,
                labService, stringToIntegerConverter, stringToDoubleConverter, i18N, resourceService);
    }
}
