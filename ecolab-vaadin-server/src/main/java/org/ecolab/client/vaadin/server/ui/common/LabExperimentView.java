package org.ecolab.client.vaadin.server.ui.common;

import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.VaadinUI;
import org.ecolab.client.vaadin.server.ui.customcomponents.ComponentErrorNotification;
import org.ecolab.client.vaadin.server.ui.styles.EcoLabTheme;
import org.ecolab.client.vaadin.server.ui.windows.ConfirmWindow;
import org.ecolab.client.vaadin.server.ui.windows.InitialDataWindow;
import org.ecolab.client.vaadin.server.ui.windows.LabFinishedWindow;
import org.ecolab.server.model.content.LabData;
import org.ecolab.server.model.content.LabVariant;
import org.ecolab.server.service.api.content.LabExperimentService;
import org.springframework.security.core.Authentication;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;

import java.util.List;

public class LabExperimentView<T extends LabData<V>, V extends LabVariant, S extends LabExperimentService<T, V>> extends LabWizard<T, V, S> {
    // ---------------------------- Графические компоненты --------------------
    private final Button downloadExperimentLogJournal = new Button("Download experiment journal", VaadinIcons.DOWNLOAD);

    private final Binder<V> variantBinder;

    private boolean hasVariantChanges;

    public LabExperimentView(I18N i18N, Authentication currentUser, InitialDataWindow<T, V> initialDataWindow, S labService, Binder<T> binder, LabFinishedWindow<T, V> labFinishedWindow, List<LabWizardStep<T, V>> labSteps, ConfirmWindow confirmWindow, VaadinUI ui, Binder<V> variantBinder) {
        super(i18N, currentUser, initialDataWindow, labService, binder, labFinishedWindow, labSteps, confirmWindow, ui);
        this.variantBinder = variantBinder;
    }


    @Override
    public void init() throws Exception {
        super.init();
        footer.addComponent(downloadExperimentLogJournal, 0);
        downloadExperimentLogJournal.setCaption(i18N.get("lab.experiment.view.experiment-journal"));
        downloadExperimentLogJournal.addStyleName(EcoLabTheme.BUTTON_PRIMARY);
        downloadExperimentLogJournal.addStyleName(EcoLabTheme.BUTTON_TINY);

        FileDownloader fileDownloader = new FileDownloader(new StreamResource(
                () -> getClass().getClassLoader().
                        getResourceAsStream("org/ecolab/server/service/impl/content/lab" + labService.getLabNumber() +
                                "/experiment/report/experimentJournal.pdf"),
                "experimentJournal.pdf"));

        fileDownloader.extend(downloadExperimentLogJournal);

        initialDataButton.setCaption(i18N.get("lab.experiment.view.experiment-journal"));
        variantBinder.addValueChangeListener(event -> {
            saveButton.setVisible(true);
            hasVariantChanges = true;
        });
    }

    /**
     * Скрывает на первых двух шагах кнопку "Журнал наблюдений"
     */
    @Override
    public void activeStepChanged(WizardStepActivationEvent event) {
        super.activeStepChanged(event);
        initialDataButton.setVisible(steps.indexOf(event.getActivatedStep()) > 1);
        downloadExperimentLogJournal.setVisible(!initialDataButton.isVisible());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        super.enter(event);
        variantBinder.setBean(binder.getBean().getVariant());
    }


    @Override
    public boolean saveData(boolean showErrors) {
        if (isExperimentJournalStep() && hasVariantChanges) {
            BinderValidationStatus<V> validationStatus = variantBinder.validate();
            if (validationStatus.isOk()) {
                ui.access(() -> {
                    variantBinder.readBean(labService.updateExperimentJournal(variantBinder.getBean()));
                    hasVariantChanges = binder.hasChanges();
                    saveButton.setVisible(false);
                });
            } else {
                if (showErrors && Page.getCurrent() != null) {
                    ComponentErrorNotification.show(i18N.get("savable.save-exception-caption"), i18N.get("savable.save-exception"));
                }
                return false;
            }
        }
        return super.saveData(showErrors);
    }

    private boolean isExperimentJournalStep() {
        return steps.indexOf(currentStep) == 1;
    }
}
