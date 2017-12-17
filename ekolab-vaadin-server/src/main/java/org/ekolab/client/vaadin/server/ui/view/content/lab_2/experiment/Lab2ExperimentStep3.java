package org.ekolab.client.vaadin.server.ui.view.content.lab_2.experiment;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.view.content.lab_2.Lab2Step3;
import org.ekolab.server.model.content.lab2.Lab2Data;
import org.ekolab.server.model.content.lab2.experiment.Lab2ExperimentLog;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringComponent
@ViewScope
public class Lab2ExperimentStep3 extends Lab2Step3<Lab2ExperimentLog> {
    // ----------------------------- Графические компоненты --------------------------------

    public Lab2ExperimentStep3(I18N i18N, Binder<Lab2Data<Lab2ExperimentLog>> binder) {
        super(i18N, binder);
    }
}
