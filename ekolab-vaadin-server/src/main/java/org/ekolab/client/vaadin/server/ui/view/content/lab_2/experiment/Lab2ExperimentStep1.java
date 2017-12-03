package org.ekolab.client.vaadin.server.ui.view.content.lab_2.experiment;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import org.ekolab.client.vaadin.server.service.api.ParameterCustomizer;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.LabExperimentJournalStep;
import org.ekolab.client.vaadin.server.ui.common.UIUtils;
import org.ekolab.client.vaadin.server.ui.customcomponents.EditableGrid;
import org.ekolab.server.model.content.lab2.Lab2Data;
import org.ekolab.server.model.content.lab2.experiment.Lab2ExperimentLog;
import org.ekolab.server.service.api.content.ValidationService;
import org.springframework.security.util.FieldUtils;

import java.util.Arrays;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringComponent
@ViewScope
public class Lab2ExperimentStep1 extends LabExperimentJournalStep<Lab2Data<Lab2ExperimentLog>, Lab2ExperimentLog> {

    // ----------------------------- Графические компоненты --------------------------------
    private final EditableGrid<Double> averageSoundPressureControlPointGrid = new EditableGrid<>();

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
        fieldWidth = 150.0F;
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "name"));
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "barometricPressure"));
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "indoorsTemperature"));
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "roomSize"));
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "quantityOfSingleTypeEquipment"));
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "hemisphereRadius"));
        centerLayout.setWidth(100.0F, Unit.PERCENTAGE);
        centerLayout.setExpandRatio(secondLayout, 2.0F);
        centerLayout.setExpandRatio(firstLayout, 1.0F);
        firstLayout.setSizeFull();
        secondLayout.setSizeFull();
        secondLayout.removeAllComponents();
        secondLayout.addComponent(averageSoundPressureControlPointGrid, 0, 0, 7, 1);
        averageSoundPressureControlPointGrid.setSizeFull();
        averageSoundPressureControlPointGrid.createColumns(i18N.get("lab2.step1.random-data.point-number"),
                Arrays.asList("31,5", "63", "125", "250", "500", "1000", "2000", "4000", "8000"),
                UIUtils.getStringConverter(Double.class, i18N));
        averageSoundPressureControlPointGrid.setRowCount(4, 0.0);
    }
}
