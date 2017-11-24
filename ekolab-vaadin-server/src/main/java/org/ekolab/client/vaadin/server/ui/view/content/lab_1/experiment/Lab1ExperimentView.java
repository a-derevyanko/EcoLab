package org.ekolab.client.vaadin.server.ui.view.content.lab_1.experiment;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringView;
import org.ekolab.client.vaadin.server.ui.common.LabExperimentView;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.Lab1Step2;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.Lab1Step3;
import org.ekolab.server.common.Profiles;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1ExperimentLog;
import org.ekolab.server.service.api.content.lab1.experiment.Lab1ExperimentService;
import org.springframework.context.annotation.Profile;

/**
 * Created by Андрей on 02.04.2017.
 */
@SpringView(name = Lab1ExperimentView.NAME)
@Profile(Profiles.MODE.PROD)
public class Lab1ExperimentView extends LabExperimentView<Lab1Data<Lab1ExperimentLog>, Lab1ExperimentLog, Lab1ExperimentService> {
    public static final String NAME = "lab1experiment";

    public Lab1ExperimentView(Lab1ExperimentPresentationStep presentationStep, Lab1ExperimentStep1 step1,
                              Lab1Step2<Lab1ExperimentLog> step2, Lab1Step3<Lab1ExperimentLog> step3, Binder<Lab1ExperimentLog> variantBinder) {
        super(variantBinder);
        labSteps.add(presentationStep);
        labSteps.add(step1);
        labSteps.add(step2);
        labSteps.add(step3);
    }
}
