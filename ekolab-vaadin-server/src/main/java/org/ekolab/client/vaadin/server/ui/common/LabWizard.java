package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.customcomponents.ComponentErrorNotification;
import org.ekolab.client.vaadin.server.ui.customcomponents.ExceptionNotification;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.client.vaadin.server.ui.view.api.AutoSavableView;
import org.ekolab.server.common.Authorize;
import org.ekolab.server.model.LabData;
import org.ekolab.server.service.api.content.LabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.WizardStep;

/**
 * Created by Андрей on 19.03.2017.
 */
@PreAuthorize(Authorize.HasAuthorities.STUDENT)
public abstract class LabWizard<BEAN extends LabData> extends Wizard implements AutoSavableView {
    private final LabService<BEAN> labService;
    private final Binder<BEAN> binder;

    private final LabPresentationStep presentationStep;

    // ---------------------------- Графические компоненты --------------------
    protected final GridLayout buttons = new GridLayout(3, 1);
    protected final Button saveButton = new Button("Save", VaadinIcons.CLOUD_DOWNLOAD_O);
    protected final HorizontalLayout leftComponentsLayout = new HorizontalLayout();
    protected final HorizontalLayout additionalComponentsLayout = new HorizontalLayout();

    @Autowired
    private I18N i18N;

    @Autowired
    private ExceptionNotification exceptionNotification;

    @Autowired
    private Authentication currentUser;

    protected LabWizard(LabService<BEAN> labService, Binder<BEAN> binder, LabPresentationStep presentationStep) {
        this.labService = labService;
        this.binder = binder;
        this.presentationStep = presentationStep;
    }

    @Override
    public void init() throws Exception {
        AutoSavableView.super.init();
        mainLayout.setSizeFull();
        buttons.setSizeFull();
        buttons.setHeightUndefined();
        buttons.setSpacing(true);
        getHeader().setVisible(false);
        saveButton.setVisible(false);
        getCancelButton().setVisible(false);
        getFinishButton().setVisible(false);

        addStyleName(EkoLabTheme.PANEL_WIZARD);
        saveButton.addStyleName(EkoLabTheme.BUTTON_PRIMARY);
        getFinishButton().addStyleName(EkoLabTheme.BUTTON_PRIMARY);
        getNextButton().addStyleName(EkoLabTheme.BUTTON_PRIMARY);
        getBackButton().addStyleName(EkoLabTheme.BUTTON_PRIMARY);

        saveButton.setCaption(i18N.get("savable.save"));
        getBackButton().setCaption(i18N.get("labwizard.back"));
        getNextButton().setCaption(i18N.get("labwizard.next"));
        getFinishButton().setCaption(i18N.get("labwizard.finish"));

        getFinishButton().setIcon(VaadinIcons.FLAG_CHECKERED, i18N.get("labwizard.finish"));
        getNextButton().setIcon(VaadinIcons.ARROW_FORWARD, i18N.get("labwizard.next"));
        getBackButton().setIcon(VaadinIcons.ARROW_BACKWARD, i18N.get("labwizard.back"));

        footer.removeComponent(getCancelButton());
        footer.removeComponent(getBackButton());
        mainLayout.removeComponent(footer);

        mainLayout.addComponent(buttons);

        leftComponentsLayout.addComponent(getBackButton());
        leftComponentsLayout.addComponent(saveButton);
        buttons.setColumnExpandRatio(1, 1.0F);
        buttons.addComponent(leftComponentsLayout, 0, 0);
        buttons.addComponent(additionalComponentsLayout, 1, 0);
        buttons.addComponent(footer, 2, 0);

        buttons.setComponentAlignment(additionalComponentsLayout, Alignment.MIDDLE_CENTER);

        binder.addStatusChangeListener(event -> updateSaveButtonState());

        addStep(presentationStep);
    }

    @Override
    @Scheduled(fixedRateString = "${lab.autoSaveRate:#{60000}}", initialDelayString = "${lab.autoSaveRate:#{60000}}")
    public void autoSave() {
        saveData();
    }

    @Override
    public boolean saveData() {
        if (binder.hasChanges()) {
            try {
                binder.writeBean(binder.getBean());
                binder.readBean(labService.updateLab(binder.getBean()));
            } catch (ValidationException e) {
                ComponentErrorNotification.show(i18N.get("savable.save-exception"), e);
                return false;
            }
        }
        return true;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        BEAN uncompletedLabData = labService.getLastUncompletedLabByUser(currentUser.getName());
        if (uncompletedLabData == null) {
            BEAN newLabData = labService.startNewLab(currentUser.getName());
            binder.setBean(newLabData);
        } else {
            binder.setBean(uncompletedLabData);
        }
    }

    @Override
    public void addStep(WizardStep step, String id) {
        if (step instanceof LabWizardStep) {
            super.addStep(step, id);
            updateButtons();
        } else {
            throw new IllegalArgumentException("Wizard step should be instance of LabWizardStep!");
        }
    }

    @Override
    protected void activateStep(WizardStep step) {
        super.activateStep(step);
        additionalComponentsLayout.removeAllComponents();
        ((LabWizardStep) step).placeAdditionalComponents(additionalComponentsLayout);
        updateButtons();
    }

    @Override
    public void cancel() {
        removeAllWindows();
        super.cancel();
    }

    @Override
    public void finish() {
        binder.getBean().setCompleted(true);
        if (saveData()) {
            removeAllWindows();
            super.finish();
        }
    }

    @Override
    public void next() {
        if (saveData()) {
            removeAllWindows();
            super.next();
        }
    }

    @Override
    public void back() {
        if (saveData()) {
            removeAllWindows();
            super.back();
        }
    }

    private void updateButtons() {
        boolean lastStep = isLastStep(currentStep);
        getFinishButton().setVisible(lastStep);
        getNextButton().setVisible(!lastStep);
        getBackButton().setVisible(!isFirstStep(currentStep));
    }

    private void updateSaveButtonState() {
        saveButton.setVisible(binder.hasChanges());
    }

    private void removeAllWindows() {
        UI.getCurrent().getWindows().forEach(UI.getCurrent()::removeWindow);
    }
}
