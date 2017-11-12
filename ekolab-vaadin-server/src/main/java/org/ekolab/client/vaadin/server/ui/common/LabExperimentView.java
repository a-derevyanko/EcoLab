package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.data.Binder;
import com.vaadin.navigator.ViewChangeListener;
import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.LabVariant;

public class LabExperimentView<T extends LabData<V>, V extends LabVariant> extends LabWizard<T, V> {
    private final Binder<V> variantBinder;

    public LabExperimentView(Binder<V> variantBinder) {
        this.variantBinder = variantBinder;
    }

    @Override
    public void init() throws Exception {
        super.init();
        initialDataButton.setCaption(i18N.get("lab.random-data.view.experiment-journal"));
        variantBinder.addValueChangeListener(event -> {
            saveButton.setVisible(true);
            hasChanges = true;
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
}
