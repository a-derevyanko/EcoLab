package org.ekolab.client.vaadin.server.service;

import com.vaadin.spring.annotation.ViewScope;
import org.ekolab.server.common.Profiles;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 777Al on 30.05.2017.
 */
@Service
@ViewScope
@Profile({Profiles.MODE.DEV, Profiles.MODE.DEMO})
public class NumeratedParameterCustomizer implements ParameterCustomizer {
    private final AtomicInteger number = new AtomicInteger();

    /**
     * Возвращает префикс в виде номера поля. Т. к. бин является уникальным на каждый LabWizard,
     * нумерация уникальна в рамках каждой лабораторной
     * @return префикс с номером
     */
    @Override
    public String getParameterPrefix() {
        return number.incrementAndGet() + ". ";
    }
}
