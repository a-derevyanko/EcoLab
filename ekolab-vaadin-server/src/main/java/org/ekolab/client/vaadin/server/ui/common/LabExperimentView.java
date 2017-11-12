package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import org.ekolab.client.vaadin.server.ui.customcomponents.ComponentErrorNotification;
import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.LabVariant;
import org.ekolab.server.service.api.content.LabExperimentService;

public class LabExperimentView<T extends LabData<V>, V extends LabVariant, S extends LabExperimentService<T, V>> extends LabWizard<T, V, S> {
    private final Binder<V> variantBinder;

    private boolean hasVariantChanges;

    public LabExperimentView(Binder<V> variantBinder) {
        this.variantBinder = variantBinder;
    }

    @Override
    public void init() throws Exception {
        super.init();
        initialDataButton.setCaption(i18N.get("lab.random-data.view.experiment-journal"));
        variantBinder.addValueChangeListener(event -> {
            saveButton.setVisible(true);
            hasVariantChanges = true;
        });
    }

    /**
     * Скрывает на первых двух шагах кнопку "Журнал наблюдений"
     */
    @Override
    protected void updateButtons() {
        super.updateButtons();
        initialDataButton.setVisible(steps.indexOf(currentStep) > 1);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);
        variantBinder.setBean(binder.getBean().getVariant());
    }


    @Override
    public boolean saveData() {
        if (isExperimentJournalStep() && hasVariantChanges) {
            BinderValidationStatus<V> validationStatus = variantBinder.validate();
            if (validationStatus.isOk()) {
                ui.access(() -> {
                    variantBinder.readBean(labService.updateExperimentJournal(variantBinder.getBean()));
                    hasVariantChanges = binder.hasChanges();
                    saveButton.setVisible(false);
                });
            } else {
                if (Page.getCurrent() != null) {
                    ComponentErrorNotification.show(i18N.get("savable.save-exception"), validationStatus.getFieldValidationErrors());
                }
                return false;
            }
        }
        return super.saveData();
    }

    private boolean isExperimentJournalStep() {
        return steps.indexOf(currentStep) == 1;
    }
}
