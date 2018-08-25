package org.ecolab.client.vaadin.server.ui.view.content.lab_1.experiment;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import org.ecolab.client.vaadin.server.service.api.ParameterCustomizer;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.common.LabExperimentJournalStep;
import org.ecolab.client.vaadin.server.ui.styles.EcoLabTheme;
import org.ecolab.server.model.content.lab1.Lab1Data;
import org.ecolab.server.model.content.lab1.experiment.Lab1ExperimentLog;
import org.ecolab.server.service.api.content.ValidationService;
import org.springframework.security.util.FieldUtils;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringComponent
@ViewScope
public class Lab1ExperimentStep1 extends LabExperimentJournalStep<Lab1Data<Lab1ExperimentLog>, Lab1ExperimentLog> {

    // ----------------------------- Графические компоненты --------------------------------
    protected final Label measurementLabel = new Label("Measurement");
    protected final GridLayout secondLayout = new GridLayout(8, 2);

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
        centerLayout.addComponent(secondLayout);
        secondLayout.setMargin(true);
        secondLayout.setSpacing(true);
        secondLayout.addComponent(measurementLabel, 0, 0, 7, 0);
        secondLayout.setComponentAlignment(measurementLabel, Alignment.TOP_CENTER);

        measurementLabel.setValue(i18N.get("lab1.step1.measurement-title"));
        measurementLabel.setStyleName(EcoLabTheme.LABEL_BOLD_ITALIC);
        addField(FieldUtils.getField(Lab1ExperimentLog.class, "name"));
        addField(FieldUtils.getField(Lab1ExperimentLog.class, "time"));
        addField(FieldUtils.getField(Lab1ExperimentLog.class, "outsideAirTemperature"));
        addField(FieldUtils.getField(Lab1ExperimentLog.class, "stacksHeight"));
        addField(FieldUtils.getField(Lab1ExperimentLog.class, "stacksDiameter"));
        addTextFieldWithAverageFields(secondLayout, FieldUtils.getField(Lab1ExperimentLog.class, "steamProductionCapacity"));
        addTextFieldWithAverageFields(secondLayout, FieldUtils.getField(Lab1ExperimentLog.class, "oxygenConcentrationPoint"));
        addTextFieldWithAverageFields(secondLayout, FieldUtils.getField(Lab1ExperimentLog.class, "fuelConsumerNormalized"));
        addTextFieldWithAverageFields(secondLayout, FieldUtils.getField(Lab1ExperimentLog.class, "stackExitTemperature"));
        addTextFieldWithAverageFields(secondLayout, FieldUtils.getField(Lab1ExperimentLog.class, "flueGasNOxConcentration"));
    }
}
