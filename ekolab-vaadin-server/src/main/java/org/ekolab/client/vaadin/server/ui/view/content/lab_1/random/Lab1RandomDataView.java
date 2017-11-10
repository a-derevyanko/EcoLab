package org.ekolab.client.vaadin.server.ui.view.content.lab_1.random;

import com.vaadin.spring.annotation.SpringView;
import org.ekolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.Lab1PresentationStep;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.Lab1View;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.experiment.Lab1ExperimentStep2;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.experiment.Lab1ExperimentStep3;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Андрей on 02.04.2017.
 */
@SpringView(name = Lab1RandomDataView.NAME)
public class Lab1RandomDataView extends Lab1View<Lab1Variant> {
    public static final String NAME = "lab1randomdata";

    @Autowired
    private Lab1PresentationStep presentationStep;

    @Autowired
    private Lab1ExperimentStep2 step2;

    @Autowired
    private Lab1ExperimentStep3 step3;

    // ----------------------------- Графические компоненты --------------------------------
    @Override
    protected Collection<LabWizardStep> getLabSteps() {
        return Arrays.asList(presentationStep, step2, step3);
    }
}
