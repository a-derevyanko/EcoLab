package org.ekolab.client.vaadin.server.ui.view.content.lab_2.experiment;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.ekolab.client.vaadin.server.service.api.ParameterCustomizer;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.LabExperimentJournalStep;
import org.ekolab.client.vaadin.server.ui.common.UIUtils;
import org.ekolab.client.vaadin.server.ui.customcomponents.EditableGrid;
import org.ekolab.client.vaadin.server.ui.customcomponents.EditableGridData;
import org.ekolab.client.vaadin.server.ui.customcomponents.ListField;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.server.model.content.lab2.Lab2Data;
import org.ekolab.server.model.content.lab2.experiment.Lab2ExperimentLog;
import org.ekolab.server.service.api.content.ValidationService;
import org.springframework.security.util.FieldUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringComponent
@ViewScope
public class Lab2ExperimentStep1 extends LabExperimentJournalStep<Lab2Data<Lab2ExperimentLog>, Lab2ExperimentLog> {
    private static final List<String> CAPTIONS = Arrays.asList("31,5", "63", "125", "250", "500", "1000", "2000", "4000", "8000");

    // ----------------------------- Графические компоненты --------------------------------
    private final Label averageSoundPressureControlPointLabel = new Label();
    private final Label pointCountLabel = new Label();
    private final Label averageLabel = new Label();
    private final ComboBox<Integer> pointCountComboBox = new ComboBox<>();
    private final HorizontalLayout averageSoundPressureControlPointTitle = new HorizontalLayout(pointCountLabel, pointCountComboBox);
    private final EditableGrid<Double> averageSoundPressureControlPointGrid = new EditableGrid<>();
    private final ListField<Double> soundPressureControlPointField = new ListField<>(0.0);
    private final VerticalLayout secondLayout = new VerticalLayout(averageSoundPressureControlPointLabel, averageSoundPressureControlPointTitle
            , averageSoundPressureControlPointGrid, averageLabel, soundPressureControlPointField);

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
        centerLayout.addComponent(secondLayout);
        secondLayout.setMargin(true);
        secondLayout.setSpacing(true);
        fieldWidth = 150.0F;
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "name"));
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "barometricPressure"));
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "indoorsTemperature"));
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "roomSize"));
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "quantityOfSingleTypeEquipment"));
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "hemisphereRadius"));
        averageSoundPressureControlPointLabel.setStyleName(EkoLabTheme.LABEL_BOLD_ITALIC);
        averageSoundPressureControlPointLabel.setValue(i18N.get("lab2.step1.experiment-data.average-sound-pressure-control-point"));
        pointCountLabel.setStyleName(EkoLabTheme.LABEL_BOLD_ITALIC);
        pointCountLabel.setValue(i18N.get("lab2.step1.experiment-data.point-count"));
        averageLabel.setStyleName(EkoLabTheme.LABEL_BOLD_ITALIC);
        averageLabel.setValue(i18N.get("lab2.step1.experiment-data.average"));
        pointCountComboBox.setTextInputAllowed(false);
        pointCountComboBox.setEmptySelectionAllowed(false);
        pointCountComboBox.setItems(IntStream.range(1, 9).boxed());
        pointCountComboBox.addValueChangeListener(event -> {
            averageSoundPressureControlPointGrid.setRowCount(event.getValue(), 0.0);
            averageSoundPressureControlPointGrid.setHeightByRows(event.getValue());
        });
        averageSoundPressureControlPointGrid.createColumns(i18N, i18N.get("lab2.step1.experiment-data.point-number"),
                Arrays.asList("31,5", "63", "125", "250", "500", "1000", "2000", "4000", "8000"),
                UIUtils.getStringConverter(Double.class, i18N));

        averageSoundPressureControlPointGrid.setWidth(700.0F, Unit.PIXELS);
        soundPressureControlPointField.setWidth(700.0F, Unit.PIXELS);
        soundPressureControlPointField.createColumns(i18N, UIUtils.getStringConverter(Double.class, i18N), CAPTIONS);
        soundPressureControlPointField.setReadOnly(true);

        averageSoundPressureControlPointGrid.getEditor().addSaveListener(event -> {
            List<Double> valuesSum = new ArrayList<>(Collections.nCopies(9, 0.0));
            List<EditableGridData<Double>> editableGridData = averageSoundPressureControlPointGrid.getItems();
            for (EditableGridData<Double> data : editableGridData) {
                List<Double> values = data.getValues();
                IntStream.range(0, values.size() - 1).forEachOrdered(i -> valuesSum.set(i, valuesSum.get(i) + values.get(i)));
            }
            soundPressureControlPointField.setValue(valuesSum.stream().map(d -> d / editableGridData.size()).collect(Collectors.toList()));
        });
        pointCountComboBox.setSelectedItem(4);

        experimentLogBinder.bind(soundPressureControlPointField, Lab2ExperimentLog::getAverageSoundPressure, Lab2ExperimentLog::setAverageSoundPressure);
    }
}
