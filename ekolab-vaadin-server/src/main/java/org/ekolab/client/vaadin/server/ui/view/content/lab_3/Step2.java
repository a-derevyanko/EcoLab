package org.ekolab.client.vaadin.server.ui.view.content.lab_3;

import com.vaadin.server.CompositeErrorMessage;
import com.vaadin.server.ErrorMessage;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.HorizontalLayout;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ekolab.client.vaadin.server.ui.customcomponents.ParameterLayout;
import org.ekolab.server.model.Lab3Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.util.FieldUtils;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringComponent
@ViewScope
public class Step2 extends HorizontalLayout implements LabWizardStep {
    // ----------------------------- Графические компоненты --------------------------------
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
        setMargin(true);
        addComponent(firstFormLayout);
        addComponent(secondFormLayout);
        firstFormLayout.setCaption(i18N.get("lab3.step2.eko-characteristics"));
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
        secondFormLayout.addField(FieldUtils.getField(Lab3Data.class, "solidParticlesProportionCollectedInAsh"));
    }

    @Override
    public ErrorMessage getComponentError() {
        Set<ErrorMessage> messageList = Stream.of(super.getComponentError(),
                firstFormLayout.getComponentError(),  secondFormLayout.getComponentError())
                .filter(Objects::nonNull).collect(Collectors.toSet());
        return messageList.isEmpty() ? null : new CompositeErrorMessage(messageList);
    }
}
