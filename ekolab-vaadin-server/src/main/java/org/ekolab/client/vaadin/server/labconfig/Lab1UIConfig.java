package org.ekolab.client.vaadin.server.labconfig;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.ViewScope;
import org.ekolab.client.vaadin.server.service.api.ParameterCustomizer;
import org.ekolab.client.vaadin.server.service.api.ResourceService;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.customcomponents.ParameterLayout;
import org.ekolab.client.vaadin.server.ui.customcomponents.ParameterWithFormulaeLayout;
import org.ekolab.client.vaadin.server.ui.windows.ResourceWindow;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.experiment.Lab1ExperimentLog;
import org.ekolab.server.model.content.lab1.random.Lab1RandomVariant;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.ekolab.server.service.api.content.ValidationService;
import org.ekolab.server.service.api.content.lab1.experiment.Lab1ExperimentService;
import org.ekolab.server.service.api.content.lab1.random.Lab1RandomService;
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
    public Binder<Lab1Data<Lab1RandomVariant>> lab1Binder() {
        return new BeanValidationBinder<Lab1Data<Lab1RandomVariant>>((Class) Lab1Data.class);
    }

    @Bean
    @ViewScope
    public Binder<Lab1Data<Lab1ExperimentLog>> lab1ExperimentBinder() {
        return new BeanValidationBinder<Lab1Data<Lab1ExperimentLog>>((Class) Lab1Data.class);
    }

    @Bean
    @ViewScope
    public Binder<Lab1Variant> lab1VariantBinder() {
        return new BeanValidationBinder<>(Lab1Variant.class);
    }

    @Bean
    @ViewScope
    public Binder<Lab1ExperimentLog> lab1ExperimentLogBinder() {
        return new BeanValidationBinder<>(Lab1ExperimentLog.class);
    }

    @Bean
    @PrototypeScope
    public ParameterLayout<Lab1Data<Lab1RandomVariant>, Lab1RandomVariant> lab1ParameterLayout1(
            Lab1RandomService labService,
            I18N i18N,
            ResourceService resourceService,
            ParameterCustomizer parameterCustomizer,
            ValidationService validationService, ResourceWindow resourceWindow) {
        return new ParameterLayout<>("content/lab1/", lab1Binder(), labService,
                i18N, resourceService, parameterCustomizer, validationService, resourceWindow);
    }

    @Bean
    @PrototypeScope
    public ParameterWithFormulaeLayout<Lab1Data<Lab1RandomVariant>, Lab1RandomVariant> lab1ParameterWithFormulaeLayout1(
            Lab1RandomService labService,
            I18N i18N,
            ResourceService resourceService,
            ParameterCustomizer parameterCustomizer,
            ValidationService validationService, ResourceWindow resourceWindow) {
        return new ParameterWithFormulaeLayout<>("content/lab1/", lab1Binder(), labService,
                i18N, resourceService, parameterCustomizer, validationService, resourceWindow);
    }
    @Bean
    @PrototypeScope
    public ParameterLayout<Lab1Data<Lab1ExperimentLog>, Lab1ExperimentLog> lab1ExperimentParameterLayout1(
            Lab1ExperimentService labService,
            I18N i18N,
            ResourceService resourceService,
            ParameterCustomizer parameterCustomizer,
            ValidationService validationService, ResourceWindow resourceWindow) {
        return new ParameterLayout<>("content/lab1/", lab1ExperimentBinder(), labService,
                i18N, resourceService, parameterCustomizer, validationService, resourceWindow);
    }

    @Bean
    @PrototypeScope
    public ParameterWithFormulaeLayout<Lab1Data<Lab1ExperimentLog>, Lab1ExperimentLog> lab1ExperimentParameterWithFormulaeLayout1(
            Lab1ExperimentService labService,
            I18N i18N,
            ResourceService resourceService,
            ParameterCustomizer parameterCustomizer,
            ValidationService validationService, ResourceWindow resourceWindow) {
        return new ParameterWithFormulaeLayout<>("content/lab1/", lab1ExperimentBinder(), labService,
                i18N, resourceService, parameterCustomizer, validationService, resourceWindow);
    }
}
