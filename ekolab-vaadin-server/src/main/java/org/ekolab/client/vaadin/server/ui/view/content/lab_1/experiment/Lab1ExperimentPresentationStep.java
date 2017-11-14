package org.ekolab.client.vaadin.server.ui.view.content.lab_1.experiment;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import org.ekolab.client.vaadin.server.service.api.PresentationService;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.ExperimentLabPresentationStep;

/**
 * Created by 777Al on 03.04.2017.
 */
@SpringComponent
@ViewScope
public class Lab1ExperimentPresentationStep extends ExperimentLabPresentationStep {
    // ---------------------------- Графические компоненты --------------------
    protected Lab1ExperimentPresentationStep(I18N i18N, PresentationService presentationService) {
        super(i18N, presentationService);
    }

    @Override
    protected int getLabNumber() {
        return 1;
    }
}
