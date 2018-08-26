package org.ecolab.client.vaadin.server.ui.view.content.lab_3;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ecolab.client.vaadin.server.ui.customcomponents.ParameterLayout;
import org.ecolab.client.vaadin.server.ui.styles.EcoLabTheme;
import org.ecolab.server.model.content.lab3.Lab3Data;
import org.ecolab.server.model.content.lab3.Lab3Variant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.util.FieldUtils;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringComponent
@ViewScope
public class Lab3Step4 extends GridLayout implements LabWizardStep<Lab3Data, Lab3Variant> {
    // ----------------------------- Графические компоненты --------------------------------
    private final Label captionLabel = new Label("Pollution calc result");

    private final I18N i18N;

    private final ParameterLayout<Lab3Data, Lab3Variant> firstFormLayout;

    private final ParameterLayout<Lab3Data, Lab3Variant> secondFormLayout;

    @Autowired
    public Lab3Step4(I18N i18N, ParameterLayout<Lab3Data, Lab3Variant> firstFormLayout, ParameterLayout<Lab3Data, Lab3Variant> secondFormLayout) {
        this.i18N = i18N;
        this.firstFormLayout = firstFormLayout;
        this.secondFormLayout = secondFormLayout;
    }

    @Override
    public void init() {
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
        captionLabel.addStyleName(EcoLabTheme.LABEL_BOLD_ITALIC);

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
