package org.ecolab.client.vaadin.server.labconfig;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.ViewScope;
import org.ecolab.client.vaadin.server.service.api.ParameterCustomizer;
import org.ecolab.client.vaadin.server.service.api.ResourceService;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.customcomponents.ParameterLayout;
import org.ecolab.client.vaadin.server.ui.windows.ResourceWindow;
import org.ecolab.server.model.content.lab3.Lab3Data;
import org.ecolab.server.model.content.lab3.Lab3Variant;
import org.ecolab.server.service.api.content.ValidationService;
import org.ecolab.server.service.api.content.lab3.Lab3Service;
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
        return new BeanValidationBinder<>(Lab3Data.class);
    }

    @Bean
    @PrototypeScope
    public ParameterLayout<Lab3Data, Lab3Variant> lab3ParameterLayout(Binder<Lab3Data> binder,
                                                                   Lab3Service labService,
                                                                   I18N i18N,
                                                                   ResourceService resourceService,
                                                                   ParameterCustomizer parameterCustomizer,
                                                                   ValidationService validationService, ResourceWindow resourceWindow) {
        return new ParameterLayout<>("content/lab3/", binder, labService,
                i18N, resourceService, parameterCustomizer, validationService, resourceWindow);
    }
}
