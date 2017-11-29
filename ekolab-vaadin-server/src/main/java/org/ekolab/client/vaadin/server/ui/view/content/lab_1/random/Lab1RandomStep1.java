package org.ekolab.client.vaadin.server.ui.view.content.lab_1.random;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ekolab.client.vaadin.server.ui.customcomponents.ParameterWithFormulaeLayout;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.random.Lab1RandomVariant;
import org.springframework.security.util.FieldUtils;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringComponent
@ViewScope
public class Lab1RandomStep1 extends HorizontalLayout implements LabWizardStep<Lab1Data<Lab1RandomVariant>, Lab1RandomVariant> {
    private final I18N i18N;

    private final ParameterWithFormulaeLayout<Lab1Data<Lab1RandomVariant>, Lab1RandomVariant> firstFormLayout;

    private final ParameterWithFormulaeLayout<Lab1Data<Lab1RandomVariant>, Lab1RandomVariant> secondFormLayout;

    public Lab1RandomStep1(I18N i18N, ParameterWithFormulaeLayout<Lab1Data<Lab1RandomVariant>, Lab1RandomVariant> firstFormLayout,
                           ParameterWithFormulaeLayout<Lab1Data<Lab1RandomVariant>, Lab1RandomVariant> secondFormLayout) {
        this.i18N = i18N;
        this.firstFormLayout = firstFormLayout;
        this.secondFormLayout = secondFormLayout;
    }

    // ----------------------------- Графические компоненты --------------------------------
    private final Label nameLabel = new Label();

    @Override
    public void init() {
        LabWizardStep.super.init();
        setSizeFull();
        setMargin(true);
        addComponent(firstFormLayout);
        addComponent(secondFormLayout);
        setCaption(i18N.get("lab1.step1.general-data"));

        nameLabel.addStyleName(EkoLabTheme.LABEL_BOLD_ITALIC);
        nameLabel.setValue(i18N.get("lab1.step1.random-data.boiler"));

        firstFormLayout.addCaption("name", 0);
        firstFormLayout.addComponent(nameLabel, 1, 0, 2, 0);
        firstFormLayout.addInfoButton("name", 0);
        firstFormLayout.insertRow(firstFormLayout.getRows());

        firstFormLayout.addField(FieldUtils.getField(Lab1Data.class, "outsideAirTemperature"));
        firstFormLayout.addField(FieldUtils.getField(Lab1Data.class, "stacksHeight"));
        firstFormLayout.addField(FieldUtils.getField(Lab1Data.class, "stacksDiameter"));
        firstFormLayout.addField(FieldUtils.getField(Lab1Data.class, "steamProductionCapacity"));

        secondFormLayout.addField(FieldUtils.getField(Lab1Data.class, "oxygenConcentrationPoint"));
        secondFormLayout.addField(FieldUtils.getField(Lab1Data.class, "fuelConsumerNormalized"));
        secondFormLayout.addField(FieldUtils.getField(Lab1Data.class, "stackExitTemperature"));
        secondFormLayout.addField(FieldUtils.getField(Lab1Data.class, "flueGasNOxConcentration"));
    }
}
