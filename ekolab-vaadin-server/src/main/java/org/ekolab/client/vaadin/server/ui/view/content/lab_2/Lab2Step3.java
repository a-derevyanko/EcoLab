package org.ekolab.client.vaadin.server.ui.view.content.lab_2;

import com.vaadin.data.Binder;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ekolab.client.vaadin.server.ui.customcomponents.EditableGridData;
import org.ekolab.server.model.content.lab2.Lab2Data;
import org.ekolab.server.model.content.lab2.Lab2Variant;

public abstract class Lab2Step3<V extends Lab2Variant> extends VerticalLayout implements LabWizardStep<Lab2Data<V>, V> {
    private final I18N i18N;

    private final Binder<Lab2Data<V>> binder;

    // ----------------------------- Графические компоненты --------------------------------
    private final Grid<EditableGridData<Double>> resultGrid = new Grid<>();

    protected Lab2Step3(I18N i18N,
                     Binder<Lab2Data<V>> binder) {
        this.i18N = i18N;
        this.binder = binder;
    }

    @Override
    public void init() {
        LabWizardStep.super.init();
        setSizeFull();
        setMargin(true);
        addComponent(resultGrid);
        setCaption(i18N.get("lab2.step2.general-data"));
    }

    @Override
    public void beforeEnter() {
        resultGrid.setItems(binder.getBean().getCalculationResult().entrySet().stream().map(e -> new EditableGridData<>(e.getKey(), e.getValue())));
    }
}
