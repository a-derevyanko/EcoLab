package org.ekolab.client.vaadin.server.ui.view.content.lab_1.random;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringView;
import org.ekolab.client.vaadin.server.ui.common.LabWizard;
import org.ekolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.Lab1PresentationStep;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.experiment.Lab1ExperimentStep2;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.experiment.Lab1ExperimentStep3;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.service.api.content.lab1.Lab1Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Андрей on 02.04.2017.
 */
@SpringView(name = Lab1RandomDataView.NAME)
public class Lab1RandomDataView extends LabWizard<Lab1Data> {
    public static final String NAME = "lab1randomdata";

    @Autowired
    private Lab1ExperimentStep2 step2;

    @Autowired
    private Lab1ExperimentStep3 step3;

    @Autowired
    public Lab1RandomDataView(Lab1Service lab1Service, Binder<Lab1Data> binder, Lab1PresentationStep presentationStep) {
        super(lab1Service, binder, presentationStep);
    }

    // ----------------------------- Графические компоненты --------------------------------
    @Override
    protected Collection<LabWizardStep> getLabSteps() {
        return Arrays.asList(step2, step3);
    }
}
