package org.ekolab.client.vaadin.server.ui.view.content.lab_3;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringView;
import org.ekolab.client.vaadin.server.ui.common.LabWizard;
import org.ekolab.server.model.content.lab3.Lab3Data;
import org.ekolab.server.service.api.content.lab3.Lab3Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Андрей on 02.04.2017.
 */
@SpringView(name = Lab3View.NAME)
public class Lab3View extends LabWizard<Lab3Data> {
    public static final String NAME = "lab3view";

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

    @Autowired
    private Lab3Step6 step6;

    @Autowired
    public Lab3View(Lab3Service lab3Service, Binder<Lab3Data> binder, Lab3PresentationStep presentationStep) {
        super(lab3Service, binder, presentationStep);
    }

    // ----------------------------- Графические компоненты --------------------------------

    @Override
    public void init() throws Exception {
        super.init();
        addStep(step1);
        addStep(step2);
        addStep(step3);
        addStep(step4);
        addStep(step5);
        addStep(step6);

        //todo binder.readBean(/*Загрузить данные*/);
    }
}
