package org.ekolab.client.vaadin.server.ui.view.content.lab_3;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ekolab.client.vaadin.server.ui.customcomponents.ParameterLayout;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringView
public class Step1 extends HorizontalLayout implements LabWizardStep {
    @Autowired
    private I18N i18N;

    @Autowired
    private ParameterLayout<Lab3Data> firstFormLayout;

    @Autowired
    private ParameterLayout<Lab3Data> secondFormLayout;

    // ----------------------------- Графические компоненты --------------------------------
    private final TextField powerField = new TextField("Power");
    private final TextField blockCountField = new TextField("Block count");
    private final TextField areaField = new TextField("Area");
    private final TextField steamProductionField = new TextField("Steam Production");
    private final TextField chimneyCountField = new TextField("Chimney count");
    private final TextField chimneyHeightField = new TextField("Chimney height");
    private final TextField chimneyDiameterField = new TextField("Chimney diameter");
    private final TextField windDirectionField = new TextField("Wind Direction");
    private final TextField windSpeedField = new TextField("Wind speed");

    @Override
    public void init() {
        setSizeFull();
        addComponent(firstFormLayout);
        setMargin(true);
        firstFormLayout.setMargin(true);
        firstFormLayout.setSizeFull();
        firstFormLayout.addStyleName(EkoLabTheme.LAYOUT_LAB);
        firstFormLayout.setCaption(i18N.get("lab1.step1.power-station-characteristics"));
        firstFormLayout.addComponent(powerField);
        firstFormLayout.addComponent(blockCountField);
        firstFormLayout.addComponent(areaField);
        firstFormLayout.addComponent(steamProductionField);
        firstFormLayout.addComponent(chimneyCountField);
        firstFormLayout.addComponent(chimneyHeightField);
        firstFormLayout.addComponent(chimneyDiameterField);
        firstFormLayout.addComponent(windDirectionField);
        firstFormLayout.addComponent(windSpeedField);/*
        lab3DataBinder.forField(powerField).withConverter(strToInt).
                withValidator(validators.intValidator(0, 1000000)).bind(Lab3Data::getPower, Lab3Data::setPower);

        lab3DataBinder.forField(steamProductionField).withConverter(strToDouble).
                withValidator(validators.doubleValidator(0.0, 1000000.0)).bind(Lab3Data::getSteamProduction, Lab3Data::setSteamProduction);*/
    }

    @Override
    public boolean onBack() {
        return false;
    }

    @Override
    public void saveData() {

    }
}
