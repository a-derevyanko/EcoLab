package org.ekolab.client.vaadin.server.ui.demo;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringView;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.Lab1PresentationStep;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.Lab1TestStep;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.experiment.Lab1ExperimentView;
import org.ekolab.server.common.Profiles;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.service.api.content.lab1.Lab1Service;
import org.springframework.context.annotation.Profile;

/**
 * Created by Андрей on 02.04.2017.
 */
@SpringView(name = DemoLab1ExperimentView.NAME)
@Profile(value = {Profiles.MODE.DEMO, Profiles.MODE.DEV})
public class DemoLab1ExperimentView extends Lab1ExperimentView {
    public DemoLab1ExperimentView(Lab1Service lab1Service, Binder<Lab1Data> binder, Lab1PresentationStep presentationStep, Lab1TestStep testStep) {
        super(lab1Service, binder, presentationStep, testStep);
    }

    @Override
    protected boolean checkJournalFilled() {
        super.checkJournalFilled();
        return true;
    }
}
