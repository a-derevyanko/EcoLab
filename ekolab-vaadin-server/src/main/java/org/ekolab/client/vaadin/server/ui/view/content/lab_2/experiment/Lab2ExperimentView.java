package org.ekolab.client.vaadin.server.ui.view.content.lab_2.experiment;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringView;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.VaadinUI;
import org.ekolab.client.vaadin.server.ui.common.LabExperimentView;
import org.ekolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ekolab.client.vaadin.server.ui.windows.ConfirmWindow;
import org.ekolab.client.vaadin.server.ui.windows.InitialDataWindow;
import org.ekolab.client.vaadin.server.ui.windows.LabFinishedWindow;
import org.ekolab.server.common.Profiles;
import org.ekolab.server.model.content.lab2.Lab2Data;
import org.ekolab.server.model.content.lab2.experiment.Lab2ExperimentLog;
import org.ekolab.server.service.api.content.lab2.experiment.Lab2ExperimentService;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * Created by Андрей on 02.04.2017.
 */
@SpringView(name = Lab2ExperimentView.NAME)
public class Lab2ExperimentView extends LabExperimentView<Lab2Data<Lab2ExperimentLog>, Lab2ExperimentLog, Lab2ExperimentService> {
    public static final String NAME = "lab2experiment";


    public Lab2ExperimentView(I18N i18N, Authentication currentUser, InitialDataWindow<Lab2Data<Lab2ExperimentLog>, Lab2ExperimentLog> initialDataWindow, Lab2ExperimentService labService, Binder<Lab2Data<Lab2ExperimentLog>> binder, LabFinishedWindow<Lab2Data<Lab2ExperimentLog>, Lab2ExperimentLog> labFinishedWindow, List<LabWizardStep<Lab2Data<Lab2ExperimentLog>, Lab2ExperimentLog>> labSteps, ConfirmWindow confirmWindow, VaadinUI ui, Binder<Lab2ExperimentLog> variantBinder) {
        super(i18N, currentUser, initialDataWindow, labService, binder, labFinishedWindow, labSteps, confirmWindow, ui, variantBinder);
    }
}
