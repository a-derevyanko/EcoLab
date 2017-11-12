package org.ekolab.client.vaadin.server.ui.view.content.lab_1.experiment;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.TextField;
import org.ekolab.client.vaadin.server.service.api.ParameterCustomizer;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.LabExperimentJournalStep;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1ExperimentLog;
import org.ekolab.server.service.api.content.ValidationService;
import org.springframework.security.util.FieldUtils;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringComponent
@ViewScope
public class Lab1ExperimentStep1 extends LabExperimentJournalStep<Lab1Data<Lab1ExperimentLog>, Lab1ExperimentLog> {

    // ----------------------------- Графические компоненты --------------------------------
    private final DateTimeField timeField = new DateTimeField();
    private final TextField objectNameField = new TextField();
    private final TextField outsideAirTemperatureField = new TextField();
    private final TextField stacksHeightField = new TextField();
    private final TextField stacksDiameterField = new TextField();
    private final TextField steamProductionCapacityField = new TextField();
    private final TextField oxygenConcentrationPointField = new TextField();
    private final TextField fuelConsumerField = new TextField();
    private final TextField stackExitTemperatureField = new TextField();
    private final TextField flueGasNOxConcentrationField = new TextField();

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
        addField(objectNameField, FieldUtils.getField(Lab1ExperimentLog.class, "name"), firstLayout, 0);
        addField(timeField, FieldUtils.getField(Lab1ExperimentLog.class, "time"), firstLayout, 1);
        addField(outsideAirTemperatureField, FieldUtils.getField(Lab1ExperimentLog.class, "outsideAirTemperature"), firstLayout, 2);
        addField(stacksHeightField, FieldUtils.getField(Lab1ExperimentLog.class, "stacksHeight"), firstLayout, 3);
        addField(stacksDiameterField, FieldUtils.getField(Lab1ExperimentLog.class, "stacksDiameter"), firstLayout, 4);
        addField(steamProductionCapacityField, FieldUtils.getField(Lab1ExperimentLog.class, "steamProductionCapacity"), secondLayout, 0);
        addField(oxygenConcentrationPointField, FieldUtils.getField(Lab1ExperimentLog.class, "oxygenConcentrationPoint"), secondLayout, 1);
        addField(fuelConsumerField, FieldUtils.getField(Lab1ExperimentLog.class, "fuelConsumerNormalized"), secondLayout, 2);
        addField(stackExitTemperatureField, FieldUtils.getField(Lab1ExperimentLog.class, "stackExitTemperature"), secondLayout, 3);
        addField(flueGasNOxConcentrationField, FieldUtils.getField(Lab1ExperimentLog.class, "flueGasNOxConcentration"), secondLayout, 4);
    }
}
