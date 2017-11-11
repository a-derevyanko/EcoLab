package org.ekolab.client.vaadin.server.ui.view.content.lab_1.random;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import org.ekolab.client.vaadin.server.ui.common.LabPresentationStep;

/**
 * Created by 777Al on 03.04.2017.
 */
@SpringComponent
@ViewScope
public class Lab1RandomPresentationStep extends LabPresentationStep {
    // ---------------------------- Графические компоненты --------------------
    @Override
    protected int getLabNumber() {
        return 1;
    }
}
