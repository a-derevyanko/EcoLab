package org.ekolab.client.vaadin.server.ui.view.content;

import com.vaadin.ui.HorizontalLayout;
import org.ekolab.client.vaadin.server.service.api.ResourceService;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ekolab.client.vaadin.server.ui.windows.ResourceWindow;

/**
 * Created by 777Al on 06.04.2017.
 */
public abstract class LabStepWithHelp extends HorizontalLayout implements LabWizardStep {
    protected boolean needShowHelp = true;

    protected final I18N i18N;

    protected final ResourceWindow resourceWindow;

    protected final ResourceService resourceService;

    // ----------------------------- Графические компоненты --------------------------------

    public LabStepWithHelp(I18N i18N, ResourceWindow resourceWindow, ResourceService resourceService) {
        this.i18N = i18N;
        this.resourceWindow = resourceWindow;
        this.resourceService = resourceService;
    }

    @Override
    public void init() {
        LabWizardStep.super.init();
    }

    @Override
    public void beforeEnter() {
        if (needShowHelp) {
            resourceWindow.show(new ResourceWindow.ResourceWindowSettings(i18N.get("lab.help-before"),
                    resourceService.getHtmlData("content/", "helpBeforeLab.svg"), true));
            needShowHelp = false;
        }
    }
}
