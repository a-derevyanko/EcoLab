package org.ecolab.client.vaadin.server.ui.view.content.lab_2.experiment;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import org.ecolab.client.vaadin.server.service.api.PresentationService;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.common.ExperimentLabPresentationStep;
import org.ecolab.server.model.content.lab2.Lab2Data;
import org.ecolab.server.model.content.lab2.experiment.Lab2ExperimentLog;

/**
 * Created by 777Al on 03.04.2017.
 */
@SpringComponent
@ViewScope
public class Lab2ExperimentPresentationStep extends ExperimentLabPresentationStep<Lab2Data<Lab2ExperimentLog>, Lab2ExperimentLog> {
    // ---------------------------- Графические компоненты --------------------
    protected Lab2ExperimentPresentationStep(I18N i18N, PresentationService presentationService) {
        super(i18N, presentationService);
    }

    @Override
    protected int getLabNumber() {
        return 2;
    }
}
