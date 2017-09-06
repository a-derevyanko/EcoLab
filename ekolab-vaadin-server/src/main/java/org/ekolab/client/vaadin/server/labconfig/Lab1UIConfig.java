package org.ekolab.client.vaadin.server.labconfig;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.ViewScope;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.service.ParameterCustomizer;
import org.ekolab.client.vaadin.server.service.ResourceService;
import org.ekolab.client.vaadin.server.ui.customcomponents.ParameterLayout;
import org.ekolab.client.vaadin.server.ui.customcomponents.ParameterWithFormulaeLayout;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.ekolab.server.service.api.content.lab1.Lab1Service;
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
    @ViewScope
    public Binder<Lab1Variant> lab1VariantBinder() {
        return new Binder<>(Lab1Variant.class);
    }

    @Bean
    @PrototypeScope
    public ParameterLayout<Lab1Data> parameterLayout1(
            Lab1Service labService,
            I18N i18N,
            ResourceService resourceService,
            ParameterCustomizer parameterCustomizer) {
        return new ParameterLayout<>("content/lab1/", lab1Binder(), labService,
                i18N, resourceService, parameterCustomizer);
    }

    @Bean
    @PrototypeScope
    public ParameterWithFormulaeLayout<Lab1Data> parameterWithFormulaeLayout1(
            Lab1Service labService,
            I18N i18N,
            ResourceService resourceService,
            ParameterCustomizer parameterCustomizer) {
        return new ParameterWithFormulaeLayout<>("content/lab1/", lab1Binder(), labService,
                i18N, resourceService, parameterCustomizer);
    }
}
