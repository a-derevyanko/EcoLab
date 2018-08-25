package org.ecolab.client.vaadin.server.ui.view.content.lab_1;

import com.vaadin.spring.annotation.SpringView;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.common.LabTestWizard;
import org.ecolab.client.vaadin.server.ui.windows.LabTestFinishedWindow;
import org.ecolab.server.common.Role;
import org.ecolab.server.model.LabMode;
import org.ecolab.server.service.api.content.UserLabService;
import org.ecolab.server.service.api.content.lab1.experiment.Lab1ExperimentService;
import org.ecolab.server.service.api.content.lab1.random.Lab1RandomService;
import org.springframework.security.core.Authentication;
import org.vaadin.spring.annotation.PrototypeScope;

import javax.annotation.security.RolesAllowed;

/**
 * Created by 777Al on 03.04.2017.
 */
@RolesAllowed(Role.STUDENT)
@PrototypeScope // При повторном входе тест должен быть другим
@SpringView(name = Lab1TestView.NAME)
public class Lab1TestView extends LabTestWizard {
    public static final String NAME = "lab1test";
    // ---------------------------- Графические компоненты --------------------

    protected Lab1TestView(I18N i18N, UserLabService userLabService,
                           Lab1RandomService lab1RandomService, Lab1ExperimentService lab1ExperimentService,
                           Authentication currentUser, LabTestFinishedWindow labTestFinishedWindow) {
        super(i18N,
                userLabService,
                userLabService.getCompletedLabs(currentUser.getName()).get(1) == LabMode.RANDOM ?
                        lab1RandomService : lab1ExperimentService,
                currentUser,
                labTestFinishedWindow);
    }

}
