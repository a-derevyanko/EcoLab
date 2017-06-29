package org.ekolab.client.vaadin.server.ui.view.content.lab_3;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ekolab.client.vaadin.server.ui.customcomponents.ParameterLayout;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.server.model.content.lab3.Lab3Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.util.FieldUtils;

import java.io.IOException;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringComponent
@ViewScope
public class Lab3Step4 extends GridLayout implements LabWizardStep {
    // ----------------------------- Графические компоненты --------------------------------
    private final Label captionLabel = new Label("Pollution calc result");

    @Autowired
    private I18N i18N;

    @Autowired
    private ParameterLayout<Lab3Data> firstFormLayout;

    @Autowired
    private ParameterLayout<Lab3Data> secondFormLayout;

    @Override
    public void init() throws IOException {
        LabWizardStep.super.init();
        setSizeFull();
        setRows(2);
        setColumns(2);
        setSpacing(true);
        addComponent(captionLabel, 0, 0, 1, 0);
        addComponent(firstFormLayout, 0, 1);
        addComponent(secondFormLayout, 1, 1);
        setComponentAlignment(captionLabel, Alignment.MIDDLE_CENTER);
        setRowExpandRatio(1, 1.0F);
        captionLabel.setValue(i18N.get("lab3.step4.pollution-calc-result"));
        captionLabel.addStyleName(EkoLabTheme.LABEL_BOLD_ITALIC);

        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "breakdownWindSpeed"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "bwdNo2GroundLevelConcentration"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "bwdNoGroundLevelConcentration"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "bwdSo2GroundLevelConcentration"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "bwdAshGroundLevelConcentration"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "no2AndSo2SummationGroup"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "noAndSo2SummationGroup"));

        secondFormLayout.addField(FieldUtils.getField(Lab3Data.class, "bwdMaxGroundLevelConcentrationDistance"));
        secondFormLayout.addField(FieldUtils.getField(Lab3Data.class, "windSpeedMaxNo2GroundLevelConcentration"));
        secondFormLayout.addField(FieldUtils.getField(Lab3Data.class, "windSpeedMaxNoGroundLevelConcentration"));
        secondFormLayout.addField(FieldUtils.getField(Lab3Data.class, "windSpeedMaxSo2GroundLevelConcentration"));
        secondFormLayout.addField(FieldUtils.getField(Lab3Data.class, "windSpeedMaxAshGroundLevelConcentration"));
        secondFormLayout.addField(FieldUtils.getField(Lab3Data.class, "windSpeedMaxGroundLevelConcentrationDistance"));
    }
}
