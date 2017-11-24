package org.ekolab.client.vaadin.server.ui.demo;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringView;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.Lab1Step2;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.Lab1Step3;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.experiment.Lab1ExperimentPresentationStep;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.experiment.Lab1ExperimentStep1;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.experiment.Lab1ExperimentView;
import org.ekolab.server.common.Profiles;
import org.ekolab.server.model.content.lab1.Lab1ExperimentLog;
import org.springframework.context.annotation.Profile;

/**
 * Created by Андрей on 02.04.2017.
 */
@SpringView(name = DemoLab1ExperimentView.NAME)
@Profile(value = {Profiles.MODE.DEMO, Profiles.MODE.DEV})
public class DemoLab1ExperimentView extends Lab1ExperimentView {
    public DemoLab1ExperimentView(Lab1ExperimentPresentationStep presentationStep, Lab1ExperimentStep1 step1,
                                  Lab1Step2<Lab1ExperimentLog> step2, Lab1Step3<Lab1ExperimentLog> step3, Binder<Lab1ExperimentLog> variantBinder) {
        super(presentationStep, step1, step2, step3, variantBinder);
    }
}
