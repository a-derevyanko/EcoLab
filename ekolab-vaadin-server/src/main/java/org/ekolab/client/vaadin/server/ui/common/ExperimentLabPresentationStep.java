package org.ekolab.client.vaadin.server.ui.common;

import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by 777Al on 03.04.2017.
 */
public abstract class ExperimentLabPresentationStep extends LabPresentationStep {
    @NotEmpty
    @NotNull
    protected String getPanelStyleName() {
        return EkoLabTheme.PANEL_WIZARD_EXPERIMENT_PRESENTATION;
    }

}
