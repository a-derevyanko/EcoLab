package org.ekolab.client.vaadin.server.ui.view.content.lab_1;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.common.LabTestStep;
import org.ekolab.server.service.api.content.lab1.Lab1Service;

/**
 * Created by 777Al on 03.04.2017.
 */
@SpringComponent
@ViewScope
public class Lab1TestStep extends LabTestStep {
    // ---------------------------- Графические компоненты --------------------

    protected Lab1TestStep(I18N i18N, Lab1Service labService) {
        super(i18N, labService);
    }
}
