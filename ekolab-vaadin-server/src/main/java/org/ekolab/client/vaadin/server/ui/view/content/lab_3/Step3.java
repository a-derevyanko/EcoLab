package org.ekolab.client.vaadin.server.ui.view.content.lab_3;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import org.ekolab.client.vaadin.server.ui.common.LabWizardStep;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringView
@ViewScope
public class Step3 extends HorizontalLayout implements LabWizardStep {
    // ----------------------------- Графические компоненты --------------------------------
    private final FormLayout firstFormLayout = new FormLayout();

    @Override
    public void saveData() {

    }
}
