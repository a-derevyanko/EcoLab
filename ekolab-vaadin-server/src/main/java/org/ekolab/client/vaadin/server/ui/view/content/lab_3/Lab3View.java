package org.ekolab.client.vaadin.server.ui.view.content.lab_3;

import com.vaadin.spring.annotation.SpringView;
import org.ekolab.client.vaadin.server.ui.common.LabWizard;
import org.ekolab.server.model.content.lab3.Lab3Data;
import org.ekolab.server.model.content.lab3.Lab3Variant;

/**
 * Created by Андрей on 02.04.2017.
 */
@SpringView(name = Lab3View.NAME)
public class Lab3View extends LabWizard<Lab3Data, Lab3Variant> {
    public static final String NAME = "lab3view";

    public Lab3View(Lab3PresentationStep presentationStep, Lab3Step1 step1, Lab3Step2 step2,
                    Lab3Step3 step3, Lab3Step4 step4, Lab3Step5 step5) {
        labSteps.add(presentationStep);
        labSteps.add(step1);
        labSteps.add(step2);
        labSteps.add(step3);
        labSteps.add(step4);
        labSteps.add(step5);
    }

    // ----------------------------- Графические компоненты --------------------------------

}
