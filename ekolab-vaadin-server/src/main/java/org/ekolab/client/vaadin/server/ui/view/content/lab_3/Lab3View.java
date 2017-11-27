package org.ekolab.client.vaadin.server.ui.view.content.lab_3;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringView;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.VaadinUI;
import org.ekolab.client.vaadin.server.ui.common.LabWizard;
import org.ekolab.client.vaadin.server.ui.windows.InitialDataWindow;
import org.ekolab.client.vaadin.server.ui.windows.LabFinishedWindow;
import org.ekolab.server.model.content.lab3.Lab3Data;
import org.ekolab.server.model.content.lab3.Lab3Variant;
import org.ekolab.server.service.api.content.lab3.Lab3Service;
import org.springframework.security.core.Authentication;

import java.util.Arrays;

/**
 * Created by Андрей on 02.04.2017.
 */
@SpringView(name = Lab3View.NAME)
public class Lab3View extends LabWizard<Lab3Data, Lab3Variant, Lab3Service> {
    public static final String NAME = "lab3view";

    public Lab3View(I18N i18N, Authentication currentUser, InitialDataWindow<Lab3Data, Lab3Variant>
            initialDataWindow, Lab3Service labService, Binder<Lab3Data> binder,
                    LabFinishedWindow<Lab3Data, Lab3Variant> labFinishedWindow, VaadinUI ui, Lab3PresentationStep presentationStep, Lab3Step1 step1, Lab3Step2 step2,
                    Lab3Step3 step3, Lab3Step4 step4, Lab3Step5 step5) {
        super(i18N, currentUser, initialDataWindow, labService, binder, labFinishedWindow, Arrays.asList(presentationStep, step1,
                step2,
                step3,
                step4,
                step5), ui);
    }

    // ----------------------------- Графические компоненты --------------------------------

}
