package org.ekolab.client.vaadin.server.ui.common;

import com.github.lotsabackscatter.blueimp.gallery.Gallery;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import org.ekolab.client.vaadin.server.service.api.PresentationService;
import org.ekolab.client.vaadin.server.service.api.ResourceService;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.view.LabViewUtils;
import org.ekolab.client.vaadin.server.ui.windows.ResourceWindow;

/**
 * Created by 777Al on 03.04.2017.
 */
public abstract class LabRandomPresentationStep extends LabPresentationStep {
    protected final I18N i18N;

    protected final PresentationService presentationService;

    private final ResourceWindow resourceWindow;

    private final ResourceService resourceService;

    // ---------------------------- Графические компоненты --------------------

    protected LabRandomPresentationStep(I18N i18N, PresentationService presentationService, ResourceWindow resourceWindow, ResourceService resourceService) {
        super(i18N, presentationService);
        this.i18N = i18N;
        this.presentationService = presentationService;
        this.resourceWindow = resourceWindow;
        this.resourceService = resourceService;
    }

    @Override
    public boolean onAdvance() {
        if (super.onAdvance()) {
            LabViewUtils.showHelpBeforeLab(resourceWindow, i18N, resourceService);
            return true;
        } else {
            return false;
        }
    }
}
