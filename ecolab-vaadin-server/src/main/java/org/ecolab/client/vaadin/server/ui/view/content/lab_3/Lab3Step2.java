package org.ecolab.client.vaadin.server.ui.view.content.lab_3;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.HorizontalLayout;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ecolab.client.vaadin.server.ui.customcomponents.ParameterLayout;
import org.ecolab.server.model.content.lab3.Lab3Data;
import org.ecolab.server.model.content.lab3.Lab3Variant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.util.FieldUtils;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringComponent
@ViewScope
public class Lab3Step2 extends HorizontalLayout implements LabWizardStep<Lab3Data, Lab3Variant> {
    // ----------------------------- Графические компоненты --------------------------------
    private final I18N i18N;

    private final ParameterLayout<Lab3Data, Lab3Variant> firstFormLayout;

    private final ParameterLayout<Lab3Data, Lab3Variant> secondFormLayout;

    @Autowired
    public Lab3Step2(I18N i18N, ParameterLayout<Lab3Data, Lab3Variant> firstFormLayout, ParameterLayout<Lab3Data, Lab3Variant> secondFormLayout) {
        this.i18N = i18N;
        this.firstFormLayout = firstFormLayout;
        this.secondFormLayout = secondFormLayout;
    }

    @Override
    public void init() {
        LabWizardStep.super.init();
        setSizeFull();
        setMargin(true);
        addComponent(firstFormLayout);
        addComponent(secondFormLayout);
        firstFormLayout.setCaption(i18N.get("lab3.step2.eco-characteristics"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "flueGasNOxConcentration"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "stackExitTemperature"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "outsideAirTemperature"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "excessAirRatio"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "combustionProductsVolume"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "waterVaporVolume"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "airVolume"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "no2BackgroundConcentration"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "noBackgroundConcentration"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "so2BackgroundConcentration"));
        firstFormLayout.addField(FieldUtils.getField(Lab3Data.class, "ashBackgroundConcentration"));

        secondFormLayout.setCaption(i18N.get("lab3.step2.entering-coefficients"));

        secondFormLayout.addField(FieldUtils.getField(Lab3Data.class, "sulphurOxidesFractionAssociatedByFlyAsh"));
        secondFormLayout.addField(FieldUtils.getField(Lab3Data.class, "sulphurOxidesFractionAssociatedInWetDustCollector"));
        secondFormLayout.addField(FieldUtils.getField(Lab3Data.class, "sulphurOxidesFractionAssociatedInDesulphurizationSystem"));
        secondFormLayout.addField(FieldUtils.getField(Lab3Data.class, "desulphurizationSystemRunningTime"));
        secondFormLayout.addField(FieldUtils.getField(Lab3Data.class, "boilerRunningTime"));
        secondFormLayout.addField(FieldUtils.getField(Lab3Data.class, "ashProportionEntrainedGases"));
    }
}
