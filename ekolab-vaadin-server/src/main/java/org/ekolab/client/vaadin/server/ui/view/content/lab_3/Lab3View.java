package org.ekolab.client.vaadin.server.ui.view.content.lab_3;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringView;
import org.ekolab.client.vaadin.server.ui.common.LabWizard;
import org.ekolab.client.vaadin.server.ui.view.api.AutoSavableView;
import org.ekolab.server.model.Lab3Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Андрей on 02.04.2017.
 */
@SpringView(name = Lab3View.NAME)
public class Lab3View extends LabWizard implements AutoSavableView {
    public static final String NAME = "lab3view";

    @Autowired
    private Lab3PresentationStep presentationStep;
    @Autowired
    private Step1 step1;

    @Autowired
    private Step2 step2;

    @Autowired
    private Step3 step3;

    @Autowired
    private Binder<Lab3Data> binder;

    // ----------------------------- Графические компоненты --------------------------------

    @Override
    public void saveData() {

    }

    @Override
    public void init() {
        super.init();
        addStep(presentationStep);
        addStep(step1);
        addStep(step2);
        addStep(step3);

        //todo binder.readBean(/*Загрузить данные*/);
    }

}
