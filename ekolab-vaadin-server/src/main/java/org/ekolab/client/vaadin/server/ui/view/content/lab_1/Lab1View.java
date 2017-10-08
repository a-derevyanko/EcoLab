package org.ekolab.client.vaadin.server.ui.view.content.lab_1;

import org.ekolab.client.vaadin.server.ui.common.LabWizard;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;

/**
 * Created by Андрей on 02.04.2017.
 */
public abstract class Lab1View extends LabWizard<Lab1Data, Lab1Variant> {
    @Override
    public void init() throws Exception {
        super.init();
        binder.addValueChangeListener(event -> {
            labService.updateCalculatedFields(binder.getBean());
            binder.validate(); // Кросс-валидация
        });
    }
}
