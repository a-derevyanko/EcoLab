package org.ecolab.client.vaadin.server.ui.view.content.lab_2.random;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ecolab.client.vaadin.server.ui.customcomponents.ParameterWithFormulaeLayout;
import org.ecolab.client.vaadin.server.ui.styles.EcoLabTheme;
import org.ecolab.server.model.content.lab2.Lab2Data;
import org.ecolab.server.model.content.lab2.random.Lab2RandomVariant;
import org.springframework.security.util.FieldUtils;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringComponent
@ViewScope
public class Lab2RandomStep1 extends HorizontalLayout implements LabWizardStep<Lab2Data<Lab2RandomVariant>, Lab2RandomVariant> {
    private final I18N i18N;

    private final Binder<Lab2Data<Lab2RandomVariant>> binder;

    private final ParameterWithFormulaeLayout<Lab2Data<Lab2RandomVariant>, Lab2RandomVariant> firstFormLayout;
    private final ParameterWithFormulaeLayout<Lab2Data<Lab2RandomVariant>, Lab2RandomVariant> secondFormLayout;

    public Lab2RandomStep1(I18N i18N,
                           Binder<Lab2Data<Lab2RandomVariant>> binder,
                           ParameterWithFormulaeLayout<Lab2Data<Lab2RandomVariant>, Lab2RandomVariant> firstFormLayout,
                           ParameterWithFormulaeLayout<Lab2Data<Lab2RandomVariant>, Lab2RandomVariant> secondFormLayout) {
        this.i18N = i18N;
        this.binder = binder;
        this.firstFormLayout = firstFormLayout;
        this.secondFormLayout = secondFormLayout;
    }

    // ----------------------------- Графические компоненты --------------------------------
    private final Label nameLabel = new Label();
    private final Label estimatedGeometricMeanFrequencyLabel = new Label();

    @Override
    public void init() {
        LabWizardStep.super.init();
        setSizeFull();
        setMargin(true);
        addComponent(firstFormLayout);
        addComponent(secondFormLayout);
        setCaption(i18N.get("lab2.step1.general-data"));

        nameLabel.addStyleName(EcoLabTheme.LABEL_BOLD_ITALIC);

        firstFormLayout.addCaption("name", 0);
        firstFormLayout.addComponent(nameLabel, 1, 0, 2, 0);
        firstFormLayout.addInfoButton("name", 0);
        firstFormLayout.insertRow(firstFormLayout.getRows());

        estimatedGeometricMeanFrequencyLabel.addStyleName(EcoLabTheme.LABEL_BOLD_ITALIC);

        firstFormLayout.addCaption("estimatedGeometricMeanFrequency", 1);
        firstFormLayout.addComponent(estimatedGeometricMeanFrequencyLabel, 1, 1, 2, 1);
        firstFormLayout.addInfoButton("estimatedGeometricMeanFrequency", 1);
        firstFormLayout.insertRow(firstFormLayout.getRows());

        firstFormLayout.addField(FieldUtils.getField(Lab2Data.class, "barometricPressure"));
        firstFormLayout.addField(FieldUtils.getField(Lab2Data.class, "indoorsTemperature"));
        secondFormLayout.addField(FieldUtils.getField(Lab2Data.class, "roomSize"));
        secondFormLayout.addField(FieldUtils.getField(Lab2Data.class, "quantityOfSingleTypeEquipment"));
        secondFormLayout.addField(FieldUtils.getField(Lab2Data.class, "hemisphereRadius"));
        secondFormLayout.addField(FieldUtils.getField(Lab2Data.class, "averageSoundPressure"));
    }

    @Override
    public void beforeEnter() {
        nameLabel.setValue(i18N.get(binder.getBean().getVariant().getName()));
        estimatedGeometricMeanFrequencyLabel.setValue(String.valueOf(binder.getBean().getVariant().getEstimatedGeometricMeanFrequency().value()));
    }
}
