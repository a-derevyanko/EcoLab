package org.ekolab.client.vaadin.server.ui.view.content.lab_1.experiment;

import com.vaadin.data.Binder;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.Lab1PresentationStep;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.Lab1View;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.ekolab.server.service.api.content.lab1.Lab1Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Андрей on 02.04.2017.
 */
@SpringView(name = Lab1ExperimentView.NAME)
public class Lab1ExperimentView extends Lab1View {
    public static final String NAME = "lab1experiment";

    @Autowired
    private Binder<Lab1Variant> variantBinder;

    @Autowired
    private Lab1ExperimentStep1 step1;

    @Autowired
    private Lab1Service lab1Service;

    @Autowired
    public Lab1ExperimentView(Lab1Service lab1Service, Binder<Lab1Data> binder, Lab1PresentationStep presentationStep) {
        super(lab1Service, binder, presentationStep);
    }

    @Override
    public void init() throws Exception {
        super.init();
        initialDataButton.setCaption(i18N.get("lab1.random-data.view.experiment-journal"));
        addStep(step1);
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
}
