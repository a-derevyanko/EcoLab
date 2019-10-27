package org.ecolab.client.vaadin.server.ui.view.content.lab_2.experiment;

import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatusHandler;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import org.apache.commons.math3.util.Precision;
import org.ecolab.client.vaadin.server.service.api.ParameterCustomizer;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.common.LabExperimentJournalStep;
import org.ecolab.client.vaadin.server.ui.common.UIUtils;
import org.ecolab.client.vaadin.server.ui.customcomponents.EditableGrid;
import org.ecolab.client.vaadin.server.ui.customcomponents.EditableGridData;
import org.ecolab.client.vaadin.server.ui.customcomponents.ListField;
import org.ecolab.client.vaadin.server.ui.styles.EcoLabTheme;
import org.ecolab.server.model.content.lab2.Lab2Data;
import org.ecolab.server.model.content.lab2.experiment.Lab2ExperimentLog;
import org.ecolab.server.service.api.content.ValidationService;
import org.springframework.security.util.FieldUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
    private final Label estimatedGeometricMeanFrequencyLabel = new Label();
    private final Label pointCountLabel = new Label();
    private final Label averageLabel = new Label();
    private final Label soundPressureValidationLabel = new Label();
    private final ComboBox<Integer> pointCountComboBox = new ComboBox<>();
    private final HorizontalLayout averageSoundPressureControlPointTitle = new HorizontalLayout(pointCountLabel, pointCountComboBox);
    private final EditableGrid<Double> averageSoundPressureControlPointGrid = new EditableGrid<>();
    private final ListField<Double> soundPressureControlPointField = new ListField<>(0.0);
    private final VerticalLayout secondLayout = new VerticalLayout(averageSoundPressureControlPointLabel, averageSoundPressureControlPointTitle
            , averageSoundPressureControlPointGrid, averageLabel, soundPressureControlPointField, soundPressureValidationLabel);
    private final Panel secondLayoutPanel = new Panel(secondLayout);

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
        centerLayout.addComponent(secondLayoutPanel);
        centerLayout.setWidth(1200.0f, Unit.PIXELS);
        secondLayout.setMargin(true);
        secondLayout.setSpacing(true);
        secondLayout.setSizeUndefined();
        secondLayoutPanel.setStyleName(EcoLabTheme.PANEL_BORDERLESS);
        secondLayoutPanel.setSizeFull();
        fieldWidth = 150.0F;
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "name"));
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "barometricPressure"));
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "indoorsTemperature"));
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "roomSize"));
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "quantityOfSingleTypeEquipment"));
        addField(FieldUtils.getField(Lab2ExperimentLog.class, "hemisphereRadius"));
        firstLayout.addComponent(estimatedGeometricMeanFrequencyLabel, 0, firstLayout.getRows() - 1, 4, firstLayout.getRows() - 1);
        estimatedGeometricMeanFrequencyLabel.setStyleName(EcoLabTheme.LABEL_BOLD_ITALIC);
        averageSoundPressureControlPointLabel.setStyleName(EcoLabTheme.LABEL_BOLD_ITALIC);
        averageSoundPressureControlPointLabel.setValue(i18N.get("lab2.step1.experiment-data.average-sound-pressure-control-point"));
        pointCountLabel.setStyleName(EcoLabTheme.LABEL_BOLD_ITALIC);
        pointCountLabel.setValue(i18N.get("lab2.step1.experiment-data.point-count"));
        averageLabel.setStyleName(EcoLabTheme.LABEL_BOLD_ITALIC);
        averageLabel.setValue(i18N.get("lab2.step1.experiment-data.average"));
        pointCountComboBox.setStyleName(EcoLabTheme.COMBOBOX_TINY);
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

        averageSoundPressureControlPointGrid.setWidth(650.0F, Unit.PIXELS);
        soundPressureControlPointField.setWidth(650.0F, Unit.PIXELS);
        soundPressureControlPointField.createColumns(i18N, UIUtils.getStringConverter(Double.class, i18N), CAPTIONS);
        soundPressureControlPointField.setEnabled(false);

        averageSoundPressureControlPointGrid.getEditor().addSaveListener(event -> {
            List<Double> valuesSum = new ArrayList<>(Collections.nCopies(9, 0.0));
            var editableGridData = averageSoundPressureControlPointGrid.getDataProvider().getItems();
            experimentLogBinder.getBean().setSoundPressure(new ArrayList<>());
            for (var data : editableGridData) {
                var values = data.getValues();
                experimentLogBinder.getBean().getSoundPressure().add(values);
                IntStream.range(0, values.size()).forEachOrdered(i -> valuesSum.set(i, valuesSum.get(i) + values.get(i)));
            }
            var average = valuesSum.stream().map(d -> Precision.round(d / editableGridData.size(), 3)).collect(Collectors.toList());
            soundPressureControlPointField.setValue(average);
            experimentLogBinder.getBean().setAverageSoundPressure(average);
        });

        soundPressureValidationLabel.setStyleName(EcoLabTheme.LABEL_FAILURE);
        var bindingBuilder =
                experimentLogBinder.forField(soundPressureControlPointField).withStatusLabel(soundPressureValidationLabel);
        soundPressureControlPointField.getEditor().getBinder().setValidationStatusHandler((BinderValidationStatusHandler<EditableGridData<Double>>) statusChange -> soundPressureValidationLabel.setValue(statusChange.getValidationErrors().toString()));
        UIUtils.bindField(FieldUtils.getField(Lab2ExperimentLog.class, "averageSoundPressure"),
                bindingBuilder, experimentLogBinder, validationService, i18N);
    }

    @Override
    public void beforeEnter() {
        var soundPressure = experimentLogBinder.getBean().getSoundPressure();
        var values = IntStream.range(0, soundPressure.size())
                .mapToObj(i -> new EditableGridData<>(i + 1, new ArrayList<>(soundPressure.get(i)))).collect(Collectors.toList());
        pointCountComboBox.setSelectedItem(soundPressure.size());
        averageSoundPressureControlPointGrid.setItems(values);
        estimatedGeometricMeanFrequencyLabel.setValue(i18N.get("lab2.step1.experiment-data.used-estimated-geometric-mean-frequency", experimentLogBinder.getBean().getEstimatedGeometricMeanFrequency().value()));
    }
}
