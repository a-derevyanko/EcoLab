package org.ekolab.client.vaadin.server.ui.view.content.lab_3;

import com.vaadin.spring.annotation.SpringView;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.LabTestWizard;
import org.ekolab.client.vaadin.server.ui.windows.LabTestFinishedWindow;
import org.ekolab.server.common.Role;
import org.ekolab.server.service.api.content.LabService;
import org.ekolab.server.service.api.content.UserLabService;
import org.ekolab.server.service.api.content.lab3.Lab3Service;
import org.springframework.security.core.Authentication;
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

    protected Lab3TestView(I18N i18N, UserLabService userLabService, LabService<?, ?> labService, Authentication currentUser, LabTestFinishedWindow labTestFinishedWindow) {
        super(i18N, userLabService, labService, currentUser, labTestFinishedWindow);
    }
    // ---------------------------- Графические компоненты --------------------

}
