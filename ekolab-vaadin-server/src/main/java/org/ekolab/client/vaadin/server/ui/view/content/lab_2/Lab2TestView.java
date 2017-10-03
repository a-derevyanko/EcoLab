package org.ekolab.client.vaadin.server.ui.view.content.lab_2;

import com.vaadin.spring.annotation.SpringView;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.LabTestWizard;
import org.ekolab.server.service.api.content.lab2.Lab2Service;

/**
 * Created by 777Al on 03.04.2017.
 */
@SpringView(name = Lab2TestView.NAME)
public class Lab2TestView extends LabTestWizard {
    public static final String NAME = "lab2test";
    // ---------------------------- Графические компоненты --------------------

    protected Lab2TestView(I18N i18N, Lab2Service labService) {
        super(i18N, labService);
    }
}
