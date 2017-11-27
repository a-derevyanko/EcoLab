package org.ekolab.client.vaadin.server.ui.common;

import org.ekolab.client.vaadin.server.service.api.PresentationService;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.LabVariant;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by 777Al on 03.04.2017.
 */
public abstract class ExperimentLabPresentationStep<T extends LabData<V>, V extends LabVariant> extends LabPresentationStep<T, V> {
    protected ExperimentLabPresentationStep(I18N i18N, PresentationService presentationService) {
        super(i18N, presentationService);
    }

    @NotEmpty
    @NotNull
    protected String getPanelStyleName() {
        return EkoLabTheme.PANEL_WIZARD_EXPERIMENT_PRESENTATION;
    }

}
