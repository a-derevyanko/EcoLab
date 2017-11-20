package org.ekolab.client.vaadin.server.ui.common;

import org.ekolab.client.vaadin.server.service.api.PresentationService;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by 777Al on 03.04.2017.
 */
public abstract class ExperimentLabPresentationStep extends LabPresentationStep {
    protected ExperimentLabPresentationStep(I18N i18N, PresentationService presentationService) {
        super(i18N, presentationService);
    }

    @Size(min = 1)
    @NotNull
    protected String getPanelStyleName() {
        return EkoLabTheme.PANEL_WIZARD_EXPERIMENT_PRESENTATION;
    }

}
