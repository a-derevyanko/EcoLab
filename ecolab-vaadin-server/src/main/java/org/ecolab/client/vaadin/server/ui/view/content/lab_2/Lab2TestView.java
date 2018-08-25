package org.ecolab.client.vaadin.server.ui.view.content.lab_2;

import com.vaadin.spring.annotation.SpringView;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.common.LabTestWizard;
import org.ecolab.client.vaadin.server.ui.windows.LabTestFinishedWindow;
import org.ecolab.server.common.Role;
import org.ecolab.server.model.LabMode;
import org.ecolab.server.service.api.content.UserLabService;
import org.ecolab.server.service.api.content.lab2.experiment.Lab2ExperimentService;
import org.ecolab.server.service.api.content.lab2.random.Lab2RandomService;
import org.springframework.security.core.Authentication;
import org.vaadin.spring.annotation.PrototypeScope;

import javax.annotation.security.RolesAllowed;

/**
 * Created by 777Al on 03.04.2017.
 */
@RolesAllowed(Role.STUDENT)
@PrototypeScope // При повторном входе тест должен быть другим
@SpringView(name = Lab2TestView.NAME)
public class Lab2TestView extends LabTestWizard {
    public static final String NAME = "lab2test";
    // ---------------------------- Графические компоненты --------------------

    protected Lab2TestView(I18N i18N, UserLabService userLabService, Lab2RandomService lab2RandomService,
                           Lab2ExperimentService lab2ExperimentService,
                           Authentication currentUser, LabTestFinishedWindow labTestFinishedWindow) {
        super(i18N,
                userLabService,
                userLabService.getCompletedLabs(currentUser.getName()).get(2) == LabMode.RANDOM ?
                        lab2RandomService : lab2ExperimentService,
                currentUser,
                labTestFinishedWindow);
    }

}
