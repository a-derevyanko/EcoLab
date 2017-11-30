package org.ekolab.client.vaadin.server.ui.view.content.lab_1.experiment;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import org.ekolab.client.vaadin.server.service.api.ParameterCustomizer;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.LabExperimentJournalStep;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.experiment.Lab1ExperimentLog;
import org.ekolab.server.service.api.content.ValidationService;
import org.springframework.security.util.FieldUtils;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringComponent
@ViewScope
public class Lab1ExperimentStep1 extends LabExperimentJournalStep<Lab1Data<Lab1ExperimentLog>, Lab1ExperimentLog> {

    // ----------------------------- Графические компоненты --------------------------------
    public Lab1ExperimentStep1(Binder<Lab1ExperimentLog> experimentLogBinder,
                               Binder<Lab1Data<Lab1ExperimentLog>> dataBinder,
                               I18N i18N,
                               ValidationService validationService,
                               ParameterCustomizer parameterCustomizer) {
        super(experimentLogBinder, dataBinder, i18N, validationService, parameterCustomizer);
    }

    @Override
    public void init() {
        super.init();
        addField(FieldUtils.getField(Lab1ExperimentLog.class, "name"));
        addField(FieldUtils.getField(Lab1ExperimentLog.class, "time"));
        addField(FieldUtils.getField(Lab1ExperimentLog.class, "outsideAirTemperature"));
        addField(FieldUtils.getField(Lab1ExperimentLog.class, "stacksHeight"));
        addField(FieldUtils.getField(Lab1ExperimentLog.class, "stacksDiameter"));
        addTextFieldWithAverageFields(FieldUtils.getField(Lab1ExperimentLog.class, "steamProductionCapacity"));
        addTextFieldWithAverageFields(FieldUtils.getField(Lab1ExperimentLog.class, "oxygenConcentrationPoint"));
        addTextFieldWithAverageFields(FieldUtils.getField(Lab1ExperimentLog.class, "fuelConsumerNormalized"));
        addTextFieldWithAverageFields(FieldUtils.getField(Lab1ExperimentLog.class, "stackExitTemperature"));
        addTextFieldWithAverageFields(FieldUtils.getField(Lab1ExperimentLog.class, "flueGasNOxConcentration"));
    }
}
