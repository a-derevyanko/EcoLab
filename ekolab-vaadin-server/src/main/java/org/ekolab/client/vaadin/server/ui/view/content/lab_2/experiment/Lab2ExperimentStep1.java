package org.ekolab.client.vaadin.server.ui.view.content.lab_2.experiment;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import org.ekolab.client.vaadin.server.service.api.ParameterCustomizer;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.LabExperimentJournalStep;
import org.ekolab.server.model.content.lab2.Lab2Data;
import org.ekolab.server.model.content.lab2.experiment.Lab2ExperimentLog;
import org.ekolab.server.service.api.content.ValidationService;
import org.springframework.security.util.FieldUtils;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringComponent
@ViewScope
public class Lab2ExperimentStep1 extends LabExperimentJournalStep<Lab2Data<Lab2ExperimentLog>, Lab2ExperimentLog> {

    // ----------------------------- Графические компоненты --------------------------------
    public Lab2ExperimentStep1(Binder<Lab2ExperimentLog> experimentLogBinder,
                               Binder<Lab2Data<Lab2ExperimentLog>> dataBinder,
                               I18N i18N,
                               ValidationService validationService,
                               ParameterCustomizer parameterCustomizer) {
        super(experimentLogBinder, dataBinder, i18N, validationService, parameterCustomizer);
    }

    @Override
    public void init() {
        super.init();
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "name"));
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "time"));
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "outsideAirTemperature"));
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "stacksHeight"));
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "stacksDiameter"));
        addTextFieldWithAverageFields(FieldUtils.getField(Lab2ExperimentLog.class, "steamProductionCapacity"));
        addTextFieldWithAverageFields(FieldUtils.getField(Lab2ExperimentLog.class, "oxygenConcentrationPoint"));
        addTextFieldWithAverageFields(FieldUtils.getField(Lab2ExperimentLog.class, "fuelConsumerNormalized"));
        addTextFieldWithAverageFields(FieldUtils.getField(Lab2ExperimentLog.class, "stackExitTemperature"));
        addTextFieldWithAverageFields(FieldUtils.getField(Lab2ExperimentLog.class, "flueGasNOxConcentration"));
    }
}
