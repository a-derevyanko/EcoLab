package org.ecolab.client.vaadin.server.labconfig;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.ViewScope;
import org.ecolab.client.vaadin.server.service.api.ParameterCustomizer;
import org.ecolab.client.vaadin.server.service.api.ResourceService;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.customcomponents.ParameterLayout;
import org.ecolab.client.vaadin.server.ui.customcomponents.ParameterWithFormulaeLayout;
import org.ecolab.client.vaadin.server.ui.windows.ResourceWindow;
import org.ecolab.server.model.content.lab2.Lab2Data;
import org.ecolab.server.model.content.lab2.experiment.Lab2ExperimentLog;
import org.ecolab.server.model.content.lab2.random.Lab2RandomVariant;
import org.ecolab.server.model.content.lab2.Lab2Variant;
import org.ecolab.server.service.api.content.ValidationService;
import org.ecolab.server.service.api.content.lab2.experiment.Lab2ExperimentService;
import org.ecolab.server.service.api.content.lab2.random.Lab2RandomService;
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
    public Binder<Lab2Data<Lab2RandomVariant>> lab2Binder() {
        return new BeanValidationBinder<Lab2Data<Lab2RandomVariant>>((Class) Lab2Data.class);
    }

    @Bean
    @ViewScope
    public Binder<Lab2Data<Lab2ExperimentLog>> lab2ExperimentBinder() {
        return new BeanValidationBinder<Lab2Data<Lab2ExperimentLog>>((Class) Lab2Data.class);
    }

    @Bean
    @ViewScope
    public Binder<Lab2Variant> lab2VariantBinder() {
        return new BeanValidationBinder<>(Lab2Variant.class);
    }

    @Bean
    @ViewScope
    public Binder<Lab2ExperimentLog> lab2ExperimentLogBinder() {
        return new BeanValidationBinder<>(Lab2ExperimentLog.class);
    }

    @Bean
    @PrototypeScope
    public ParameterLayout<Lab2Data<Lab2RandomVariant>, Lab2RandomVariant> lab2ParameterLayout1(
            Lab2RandomService labService,
            I18N i18N,
            ResourceService resourceService,
            ParameterCustomizer parameterCustomizer,
            ValidationService validationService, ResourceWindow resourceWindow) {
        return new ParameterLayout<>("content/lab2/", lab2Binder(), labService,
                i18N, resourceService, parameterCustomizer, validationService, resourceWindow);
    }

    @Bean
    @PrototypeScope
    public ParameterWithFormulaeLayout<Lab2Data<Lab2RandomVariant>, Lab2RandomVariant> lab2ParameterWithFormulaeLayout1(
            Lab2RandomService labService,
            I18N i18N,
            ResourceService resourceService,
            ParameterCustomizer parameterCustomizer,
            ValidationService validationService, ResourceWindow resourceWindow) {
        return new ParameterWithFormulaeLayout<>("content/lab2/", lab2Binder(), labService,
                i18N, resourceService, parameterCustomizer, validationService, resourceWindow);
    }
    @Bean
    @PrototypeScope
    public ParameterLayout<Lab2Data<Lab2ExperimentLog>, Lab2ExperimentLog> lab2ExperimentParameterLayout1(
            Lab2ExperimentService labService,
            I18N i18N,
            ResourceService resourceService,
            ParameterCustomizer parameterCustomizer,
            ValidationService validationService, ResourceWindow resourceWindow) {
        return new ParameterLayout<>("content/lab2/", lab2ExperimentBinder(), labService,
                i18N, resourceService, parameterCustomizer, validationService, resourceWindow);
    }

    @Bean
    @PrototypeScope
    public ParameterWithFormulaeLayout<Lab2Data<Lab2ExperimentLog>, Lab2ExperimentLog> lab2ExperimentParameterWithFormulaeLayout1(
            Lab2ExperimentService labService,
            I18N i18N,
            ResourceService resourceService,
            ParameterCustomizer parameterCustomizer,
            ValidationService validationService, ResourceWindow resourceWindow) {
        return new ParameterWithFormulaeLayout<>("content/lab2/", lab2ExperimentBinder(), labService,
                i18N, resourceService, parameterCustomizer, validationService, resourceWindow);
    }
}
