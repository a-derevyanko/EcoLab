package org.ekolab.client.vaadin.server.ui.view.content.lab_1.experiment;

import com.vaadin.data.Binder;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.Lab1Step2;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.Lab1Step3;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.Lab1View;
import org.ekolab.server.common.Profiles;
import org.ekolab.server.model.content.lab1.Lab1ExperimentLog;
import org.springframework.context.annotation.Profile;

/**
 * Created by Андрей on 02.04.2017.
 */
@SpringView(name = Lab1ExperimentView.NAME)
@Profile(Profiles.MODE.PROD)
public class Lab1ExperimentView extends Lab1View<Lab1ExperimentLog> {
    public static final String NAME = "lab1experiment";

    private final Binder<Lab1ExperimentLog> variantBinder;

    public Lab1ExperimentView(Lab1ExperimentPresentationStep presentationStep, Lab1ExperimentStep1 step1,
                              Lab1Step2 step2, Lab1Step3 step3, Binder<Lab1ExperimentLog> variantBinder) {
        this.variantBinder = variantBinder;
        labSteps.add(presentationStep);
        labSteps.add(step1);
        labSteps.add(step2);
        labSteps.add(step3);
    }

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

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);
        variantBinder.setBean(binder.getBean().getVariant());
    }
}
