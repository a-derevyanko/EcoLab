package org.ekolab.client.vaadin.server.ui.view.content.lab_1;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import org.ekolab.client.vaadin.server.ui.common.LabPresentationStep;

/**
 * Created by 777Al on 03.04.2017.
 */
@SpringComponent
@ViewScope
public class Lab1PresentationStep extends LabPresentationStep {
    // ---------------------------- Графические компоненты --------------------
    @Override
    protected int getLabNumber() {
        return 1;
    }
}
