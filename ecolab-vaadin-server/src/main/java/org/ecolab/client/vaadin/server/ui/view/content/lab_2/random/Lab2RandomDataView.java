package org.ecolab.client.vaadin.server.ui.view.content.lab_2.random;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringView;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.VaadinUI;
import org.ecolab.client.vaadin.server.ui.common.LabWizard;
import org.ecolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ecolab.client.vaadin.server.ui.windows.ConfirmWindow;
import org.ecolab.client.vaadin.server.ui.windows.InitialDataWindow;
import org.ecolab.client.vaadin.server.ui.windows.LabFinishedWindow;
import org.ecolab.server.model.content.lab2.Lab2Data;
import org.ecolab.server.model.content.lab2.random.Lab2RandomVariant;
import org.ecolab.server.service.api.content.lab2.random.Lab2RandomService;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * Created by Андрей on 02.04.2017.
 */
@SpringView(name = Lab2RandomDataView.NAME)
public class Lab2RandomDataView extends LabWizard<Lab2Data<Lab2RandomVariant>, Lab2RandomVariant, Lab2RandomService> {
    public static final String NAME = "lab2randomdata";

    // ----------------------------- Графические компоненты --------------------------------

    public Lab2RandomDataView(I18N i18N, Authentication currentUser, InitialDataWindow<Lab2Data<Lab2RandomVariant>, Lab2RandomVariant> initialDataWindow, Lab2RandomService labService, Binder<Lab2Data<Lab2RandomVariant>> binder, LabFinishedWindow<Lab2Data<Lab2RandomVariant>, Lab2RandomVariant> labFinishedWindow, List<LabWizardStep<Lab2Data<Lab2RandomVariant>, Lab2RandomVariant>> labSteps, ConfirmWindow confirmWindow, VaadinUI ui) {
        super(i18N, currentUser, initialDataWindow, labService, binder, labFinishedWindow, labSteps, confirmWindow, ui);
    }

}
