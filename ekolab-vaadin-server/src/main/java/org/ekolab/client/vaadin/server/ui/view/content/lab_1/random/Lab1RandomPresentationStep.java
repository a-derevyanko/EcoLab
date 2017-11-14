package org.ekolab.client.vaadin.server.ui.view.content.lab_1.random;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import org.ekolab.client.vaadin.server.service.api.PresentationService;
import org.ekolab.client.vaadin.server.service.api.ResourceService;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.LabRandomPresentationStep;
import org.ekolab.client.vaadin.server.ui.windows.ResourceWindow;

/**
 * Created by 777Al on 03.04.2017.
 */
@SpringComponent
@ViewScope
public class Lab1RandomPresentationStep extends LabRandomPresentationStep {

    // ---------------------------- Графические компоненты --------------------

    protected Lab1RandomPresentationStep(I18N i18N, PresentationService presentationService, ResourceWindow resourceWindow, ResourceService resourceService) {
        super(i18N, presentationService, resourceWindow, resourceService);
    }

    @Override
    protected int getLabNumber() {
        return 1;
    }
}
