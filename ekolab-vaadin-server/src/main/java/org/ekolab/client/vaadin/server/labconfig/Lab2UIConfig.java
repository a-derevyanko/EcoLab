package org.ekolab.client.vaadin.server.labconfig;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.ViewScope;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.service.ParameterCustomizer;
import org.ekolab.client.vaadin.server.service.ResourceService;
import org.ekolab.client.vaadin.server.ui.customcomponents.ParameterLayout;
import org.ekolab.client.vaadin.server.ui.customcomponents.ParameterWithFormulaeLayout;
import org.ekolab.server.model.content.lab2.Lab2Data;
import org.ekolab.server.service.api.content.lab3.Lab3Service;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.vaadin.spring.annotation.PrototypeScope;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringBootConfiguration
public class Lab2UIConfig {
    @Bean
    @ViewScope
    public Binder<Lab2Data> lab2Binder() {
        return new Binder<>(Lab2Data.class);
    }

    @Bean
    @PrototypeScope
    public ParameterLayout<Lab2Data> parameterLayout(
            Lab3Service labService,
            I18N i18N,
            ResourceService resourceService,
            ParameterCustomizer parameterCustomizer) {
        return new ParameterLayout<>("content/lab2/", lab2Binder(), labService,
                i18N, resourceService, parameterCustomizer);
    }

    @Bean
    @PrototypeScope
    public ParameterWithFormulaeLayout<Lab2Data> parameterWithFormulaeLayout(
            Lab3Service labService,
            I18N i18N,
            ResourceService resourceService,
            ParameterCustomizer parameterCustomizer) {
        return new ParameterWithFormulaeLayout<>("content/lab2/", lab2Binder(), labService,
                i18N, resourceService, parameterCustomizer);
    }
}
