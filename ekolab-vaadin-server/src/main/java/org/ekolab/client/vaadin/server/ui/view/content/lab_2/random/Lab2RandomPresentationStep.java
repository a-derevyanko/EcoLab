package org.ekolab.client.vaadin.server.ui.view.content.lab_2.random;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import org.ekolab.client.vaadin.server.service.api.PresentationService;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.LabPresentationStep;
import org.ekolab.server.model.content.lab2.Lab2Data;
import org.ekolab.server.model.content.lab2.random.Lab2RandomVariant;

/**
 * Created by 777Al on 03.04.2017.
 */
@SpringComponent
@ViewScope
public class Lab2RandomPresentationStep extends LabPresentationStep<Lab2Data<Lab2RandomVariant>, Lab2RandomVariant> {

    // ---------------------------- Графические компоненты --------------------
    protected Lab2RandomPresentationStep(I18N i18N, PresentationService presentationService) {
        super(i18N, presentationService);
    }

    @Override
    protected int getLabNumber() {
        return 2;
    }
}
