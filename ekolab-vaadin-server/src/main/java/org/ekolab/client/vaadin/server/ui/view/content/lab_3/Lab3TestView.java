package org.ekolab.client.vaadin.server.ui.view.content.lab_3;

import com.vaadin.spring.annotation.SpringView;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.common.LabTestWizard;
import org.ekolab.server.service.api.content.lab3.Lab3Service;

/**
 * Created by 777Al on 03.04.2017.
 */
@SpringView(name = Lab3TestView.NAME)
public class Lab3TestView extends LabTestWizard {
    public static final String NAME = "lab3test";
    // ---------------------------- Графические компоненты --------------------

    protected Lab3TestView(I18N i18N, Lab3Service labService) {
        super(i18N, labService);
    }
}
