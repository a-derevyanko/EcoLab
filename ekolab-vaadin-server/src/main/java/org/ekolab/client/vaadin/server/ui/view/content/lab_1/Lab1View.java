package org.ekolab.client.vaadin.server.ui.view.content.lab_1;

import com.vaadin.data.Binder;
import org.ekolab.client.vaadin.server.ui.common.LabWizard;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.service.api.content.lab1.Lab1Service;

/**
 * Created by Андрей on 02.04.2017.
 */
public abstract class Lab1View extends LabWizard<Lab1Data> {
    public Lab1View(Lab1Service lab1Service, Binder<Lab1Data> binder, Lab1PresentationStep presentationStep) {
        super(lab1Service, binder, presentationStep);
    }

    @Override
    public void init() throws Exception {
        super.init();
        binder.addValueChangeListener(event -> {
            labService.updateCalculatedFields(binder.getBean());
            binder.validate(); // Кросс-валидация
        });
    }
}
