package org.ecolab.client.vaadin.server.ui.view.content.lab_1.experiment;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.customcomponents.ParameterWithFormulaeLayout;
import org.ecolab.client.vaadin.server.ui.view.content.lab_1.Lab1Step3;
import org.ecolab.server.model.content.lab1.Lab1Data;
import org.ecolab.server.model.content.lab1.experiment.Lab1ExperimentLog;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringComponent
@ViewScope
public class Lab1ExperimentStep3 extends Lab1Step3<Lab1ExperimentLog> {
    public Lab1ExperimentStep3(I18N i18N, ParameterWithFormulaeLayout<Lab1Data<Lab1ExperimentLog>, Lab1ExperimentLog> firstFormLayout) {
        super(i18N, firstFormLayout);
    }
}
