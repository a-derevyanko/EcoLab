package org.ecolab.client.vaadin.server.service.impl;

import com.vaadin.spring.annotation.ViewScope;
import org.ecolab.client.vaadin.server.service.api.ParameterCustomizer;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 777Al on 30.05.2017.
 */
@Service
@ViewScope
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
