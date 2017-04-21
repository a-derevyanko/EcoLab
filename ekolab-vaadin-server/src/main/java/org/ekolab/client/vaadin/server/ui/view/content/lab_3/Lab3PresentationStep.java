package org.ekolab.client.vaadin.server.ui.view.content.lab_3;

import com.vaadin.spring.annotation.SpringView;
import org.ekolab.client.vaadin.server.ui.common.LabPresentationStep;

/**
 * Created by 777Al on 03.04.2017.
 */
@SpringView
public class Lab3PresentationStep extends LabPresentationStep {
    // ---------------------------- Графические компоненты --------------------

    @Override
    protected String getLabContentFolder() {
        return "content/lab3/presentation";
    }
}
