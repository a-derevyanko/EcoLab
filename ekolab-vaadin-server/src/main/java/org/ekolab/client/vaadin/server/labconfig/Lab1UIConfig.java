package org.ekolab.client.vaadin.server.labconfig;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.ViewScope;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.service.ParameterCustomizer;
import org.ekolab.client.vaadin.server.service.ResourceService;
import org.ekolab.client.vaadin.server.ui.customcomponents.ParameterLayout;
import org.ekolab.client.vaadin.server.ui.customcomponents.ParameterWithFormulaeLayout;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.service.api.content.lab3.Lab3Service;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.vaadin.spring.annotation.PrototypeScope;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringBootConfiguration
public class Lab1UIConfig {
    @Bean
    @ViewScope
    public Binder<Lab1Data> lab1Binder() {
        return new Binder<>(Lab1Data.class);
    }

    @Bean
    @PrototypeScope
    public ParameterLayout<Lab1Data> parameterLayout(
            Lab3Service labService,
            I18N i18N,
            ResourceService resourceService,
            ParameterCustomizer parameterCustomizer) {
        return new ParameterLayout<>("content/lab2/", lab1Binder(), labService,
                i18N, resourceService, parameterCustomizer);
    }

    @Bean
    @PrototypeScope
    public ParameterWithFormulaeLayout<Lab1Data> parameterWithFormulaeLayout(
            Lab3Service labService,
            I18N i18N,
            ResourceService resourceService,
            ParameterCustomizer parameterCustomizer) {
        return new ParameterWithFormulaeLayout<>("content/lab2/", lab1Binder(), labService,
                i18N, resourceService, parameterCustomizer);
    }
}
