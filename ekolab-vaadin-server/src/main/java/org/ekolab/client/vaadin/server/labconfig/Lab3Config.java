package org.ekolab.client.vaadin.server.labconfig;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.ViewScope;
import org.ekolab.client.vaadin.server.ui.view.content.lab_3.Lab3Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringBootConfiguration
@Lazy
public class Lab3Config {
    @Bean
    @ViewScope
    public Binder<Lab3Data> lab3Binder() {
        return new Binder<>(Lab3Data.class);
    }
}
