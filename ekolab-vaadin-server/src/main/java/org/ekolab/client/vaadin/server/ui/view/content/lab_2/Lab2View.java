package org.ekolab.client.vaadin.server.ui.view.content.lab_2;

import com.vaadin.data.Binder;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.VaadinUI;
import org.ekolab.client.vaadin.server.ui.common.LabWizard;
import org.ekolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ekolab.client.vaadin.server.ui.view.api.AutoSavableView;
import org.ekolab.client.vaadin.server.ui.windows.InitialDataWindow;
import org.ekolab.client.vaadin.server.ui.windows.LabFinishedWindow;
import org.ekolab.server.model.content.lab2.Lab2Data;
import org.ekolab.server.model.content.lab2.Lab2Variant;
import org.ekolab.server.service.api.content.lab2.Lab2Service;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * Created by Андрей on 02.04.2017.
 */
public abstract class Lab2View extends LabWizard<Lab2Data, Lab2Variant, Lab2Service> implements AutoSavableView {
    public Lab2View(I18N i18N, Authentication currentUser, InitialDataWindow<Lab2Data, Lab2Variant> initialDataWindow, Lab2Service labService, Binder<Lab2Data> binder, LabFinishedWindow<Lab2Data, Lab2Variant> labFinishedWindow, List<LabWizardStep> labSteps, VaadinUI ui) {
        super(i18N, currentUser, initialDataWindow, labService, binder, labFinishedWindow, labSteps, ui);
    }
}
