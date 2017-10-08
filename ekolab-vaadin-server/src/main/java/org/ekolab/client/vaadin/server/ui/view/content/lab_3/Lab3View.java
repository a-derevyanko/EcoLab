package org.ekolab.client.vaadin.server.ui.view.content.lab_3;

import com.vaadin.spring.annotation.SpringView;
import org.ekolab.client.vaadin.server.ui.common.LabWizard;
import org.ekolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ekolab.server.model.content.lab3.Lab3Data;
import org.ekolab.server.model.content.lab3.Lab3Variant;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Андрей on 02.04.2017.
 */
@SpringView(name = Lab3View.NAME)
public class Lab3View extends LabWizard<Lab3Data, Lab3Variant> {
    public static final String NAME = "lab3view";

    @Autowired
    private Lab3PresentationStep presentationStep;

    @Autowired
    private Lab3Step1 step1;

    @Autowired
    private Lab3Step2 step2;

    @Autowired
    private Lab3Step3 step3;

    @Autowired
    private Lab3Step4 step4;

    @Autowired
    private Lab3Step5 step5;

    // ----------------------------- Графические компоненты --------------------------------

    @Override
    protected Collection<LabWizardStep> getLabSteps() {
        return Arrays.asList(presentationStep, step1, step2, step3, step4, step5);
    }
}
