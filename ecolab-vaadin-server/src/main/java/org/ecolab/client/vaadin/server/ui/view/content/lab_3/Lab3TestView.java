package org.ecolab.client.vaadin.server.ui.view.content.lab_3;

import com.vaadin.spring.annotation.SpringView;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.common.LabTestWizard;
import org.ecolab.client.vaadin.server.ui.windows.LabTestFinishedWindow;
import org.ecolab.server.common.Role;
import org.ecolab.server.service.api.content.UserLabService;
import org.ecolab.server.service.api.content.lab3.Lab3Service;
import org.vaadin.spring.annotation.PrototypeScope;

import javax.annotation.security.RolesAllowed;

/**
 * Created by 777Al on 03.04.2017.
 */
@RolesAllowed(Role.STUDENT)
@PrototypeScope // При повторном входе тест должен быть другим
@SpringView(name = Lab3TestView.NAME)
public class Lab3TestView extends LabTestWizard {
    public static final String NAME = "lab3test";

    protected Lab3TestView(I18N i18N, UserLabService userLabService, Lab3Service labService, LabTestFinishedWindow labTestFinishedWindow) {
        super(i18N, userLabService, labService, labTestFinishedWindow);
    }
    // ---------------------------- Графические компоненты --------------------

}
