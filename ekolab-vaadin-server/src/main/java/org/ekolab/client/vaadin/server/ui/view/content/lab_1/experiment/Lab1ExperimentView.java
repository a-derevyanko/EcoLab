package org.ekolab.client.vaadin.server.ui.view.content.lab_1.experiment;

import com.vaadin.data.Binder;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import org.ekolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ekolab.client.vaadin.server.ui.customcomponents.ComponentErrorNotification;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.Lab1PresentationStep;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.Lab1View;
import org.ekolab.server.common.Profiles;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.ekolab.server.service.api.content.lab1.Lab1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Андрей on 02.04.2017.
 */
@SpringView(name = Lab1ExperimentView.NAME)
@Profile(Profiles.MODE.PROD)
public class Lab1ExperimentView extends Lab1View {
    public static final String NAME = "lab1experiment";

    @Autowired
    private Binder<Lab1Variant> variantBinder;

    @Autowired
    private Lab1PresentationStep presentationStep;

    @Autowired
    private Lab1ExperimentStep1 step1;

    @Autowired
    private Lab1ExperimentStep2 step2;

    @Autowired
    private Lab1ExperimentStep3 step3;

    @Autowired
    private Lab1Service lab1Service;

    @Override
    public void init() throws Exception {
        super.init();
        initialDataButton.setCaption(i18N.get("lab.random-data.view.experiment-journal"));
    }

    /**
     * Скрывает на первых двух шагах кнопку "Журнал наблюдений"
     */
    @Override
    protected void updateButtons() {
        super.updateButtons();
        initialDataButton.setVisible(steps.indexOf(currentStep) > 1);
    }

    /**
     * Перед уходом с первой страницы необходимо проверить заполнение данных лабораторной
     */
    @Override
    public void next() {
        if (checkJournalFilled()) {
            super.next();
        }
    }

    @Override
    protected Collection<LabWizardStep> getLabSteps() {
        return Arrays.asList(presentationStep, step1, step2, step3);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Lab1Data uncompletedLabData = labService.getLastUncompletedLabByUser(currentUser.getName());
        if (uncompletedLabData == null) {
            Lab1Data newLabData = lab1Service.startNewLabWithEmptyVariant(currentUser.getName());
            binder.setBean(newLabData);
            variantBinder.setBean(newLabData.getVariant());
        } else {
            binder.setBean(uncompletedLabData);
            variantBinder.setBean(uncompletedLabData.getVariant());
        }
    }

    protected boolean checkJournalFilled() {
        if (Page.getCurrent() != null && steps.indexOf(currentStep) == 1) {
            Lab1Variant variant = binder.getBean().getVariant();
            for (Field field : variant.getClass().getDeclaredFields()) {
                ReflectionUtils.makeAccessible(field);
                Object value = ReflectionUtils.getField(field, variant);
                if (value == null) {
                    ComponentErrorNotification.show(i18N.get("savable.save-exception"), i18N.get("lab1.step1.fill-variant"));
                    return false;
                }
            }
        }
        return true;
    }
}
