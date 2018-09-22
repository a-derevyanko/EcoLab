package org.ecolab.client.vaadin.server.ui.view.content.lab_1.random;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringView;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.VaadinUI;
import org.ecolab.client.vaadin.server.ui.common.LabWizard;
import org.ecolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ecolab.client.vaadin.server.ui.windows.ConfirmWindow;
import org.ecolab.client.vaadin.server.ui.windows.InitialDataWindow;
import org.ecolab.client.vaadin.server.ui.windows.LabFinishedWindow;
import org.ecolab.server.model.content.lab1.Lab1Data;
import org.ecolab.server.model.content.lab1.random.Lab1RandomVariant;
import org.ecolab.server.service.api.content.lab1.random.Lab1RandomService;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * Created by Андрей on 02.04.2017.
 */
@SpringView(name = Lab1RandomDataView.NAME)
public class Lab1RandomDataView extends LabWizard<Lab1Data<Lab1RandomVariant>, Lab1RandomVariant, Lab1RandomService> {
    public static final String NAME = "lab1randomdata";

    public Lab1RandomDataView(I18N i18N,
                              InitialDataWindow<Lab1Data<Lab1RandomVariant>,
                                                                    Lab1RandomVariant> initialDataWindow,
                              Lab1RandomService labService,
                              Binder<Lab1Data<Lab1RandomVariant>> binder,
                              LabFinishedWindow<Lab1Data<Lab1RandomVariant>,
                                      Lab1RandomVariant> labFinishedWindow,
                              List<LabWizardStep<Lab1Data<Lab1RandomVariant>, Lab1RandomVariant>> labSteps,
                              ConfirmWindow confirmWindow,
                              VaadinUI ui,
                              @Value("${ecolab.lab.autoSaveRate:#{60000}}") long autoSaveRate) {
        super(i18N, initialDataWindow, labService, binder, labFinishedWindow, labSteps, confirmWindow, ui, autoSaveRate);
    }


    // ----------------------------- Графические компоненты --------------------------------
}
