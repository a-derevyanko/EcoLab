package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.customcomponents.ComponentErrorNotification;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.client.vaadin.server.ui.view.api.AutoSavableView;
import org.ekolab.server.common.Role;
import org.ekolab.server.model.content.LabData;
import org.ekolab.server.service.api.content.LabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.vaadin.teemu.wizards.WizardStep;

import javax.annotation.security.RolesAllowed;
import java.util.Collection;

/**
 * Created by Андрей on 19.03.2017.
 */
@RolesAllowed(Role.STUDENT)
public abstract class LabWizard<BEAN extends LabData<?>> extends Wizard implements AutoSavableView {
    // ---------------------------- Графические компоненты --------------------
    protected final GridLayout buttons = new GridLayout(3, 1);
    protected final Button saveButton = new Button("Save", VaadinIcons.CLOUD_DOWNLOAD_O);
    protected final Button initialDataButton = new Button("Initial data", VaadinIcons.CLIPBOARD_TEXT);
    protected final HorizontalLayout leftButtonsLayout = new HorizontalLayout();
    protected final HorizontalLayout firstColumnLayout = new HorizontalLayout();
    protected final HorizontalLayout secondColumnLayout = new HorizontalLayout();
    protected final HorizontalLayout thirdComponentsLayout = new HorizontalLayout();

    protected boolean hasChanges;

    @Autowired
    protected I18N i18N;

    @Autowired
    protected Authentication currentUser;

    @Autowired
    protected InitialDataWindow initialDataWindow;

    @Autowired
    protected LabService<BEAN> labService;

    @Autowired
    protected Binder<BEAN> binder;

    @Autowired
    protected LabFinishedWindow<BEAN> labFinishedWindow;

    @Override
    public void init() throws Exception {
        super.init();
        getHeader().setVisible(false);
        buttons.setSizeFull();
        buttons.setHeightUndefined();
        buttons.setSpacing(true);
        saveButton.setVisible(false);

        saveButton.addStyleName(EkoLabTheme.BUTTON_PRIMARY);
        saveButton.addStyleName(EkoLabTheme.BUTTON_TINY);
        initialDataButton.addStyleName(EkoLabTheme.BUTTON_PRIMARY);
        initialDataButton.addStyleName(EkoLabTheme.BUTTON_TINY);

        saveButton.setCaption(i18N.get("savable.save"));
        initialDataButton.setCaption(i18N.get("labwizard.initial-data"));

        mainLayout.removeComponent(footer);

        footer.addComponent(initialDataButton, 0);
        mainLayout.addComponent(buttons);

        thirdComponentsLayout.addComponent(footer);

        leftButtonsLayout.addComponent(getBackButton());
        leftButtonsLayout.addComponent(saveButton);
        firstColumnLayout.addComponent(leftButtonsLayout);
        buttons.addComponent(firstColumnLayout, 0, 0);
        buttons.addComponent(secondColumnLayout, 1, 0);
        buttons.addComponent(thirdComponentsLayout, 2, 0);

        firstColumnLayout.setSizeFull();
        secondColumnLayout.setSizeFull();
        thirdComponentsLayout.setSizeFull();

        buttons.setComponentAlignment(firstColumnLayout, Alignment.MIDDLE_LEFT);
        buttons.setComponentAlignment(secondColumnLayout, Alignment.MIDDLE_CENTER);
        buttons.setComponentAlignment(thirdComponentsLayout, Alignment.MIDDLE_RIGHT);

        thirdComponentsLayout.setComponentAlignment(footer, Alignment.MIDDLE_RIGHT);

        firstColumnLayout.setComponentAlignment(leftButtonsLayout, Alignment.MIDDLE_LEFT);

        binder.addValueChangeListener(event -> {saveButton.setVisible(true); hasChanges = true;});

        saveButton.addClickListener(event -> saveData());
        initialDataButton.addClickListener(event -> showInitialData());

        getLabSteps().forEach(this::addStep);
    }

    /**
     * Возвращает список шагов лабораторной.
     * @return список шагов лабораторной.
     */
    protected abstract Collection<LabWizardStep> getLabSteps();

    @Override
    @Scheduled(fixedRateString = "${ekolab.lab.autoSaveRate:#{60000}}", initialDelayString = "${ekolab.lab.autoSaveRate:#{60000}}")
    public void autoSave() {
        saveData();
    }

    @Override
    public boolean saveData() {
        if (hasUnsavedData()) {
            BinderValidationStatus<BEAN> validationStatus = binder.validate();
            if (validationStatus.isOk()) {
                binder.readBean(labService.updateLab(binder.getBean()));
                hasChanges = binder.hasChanges();
                saveButton.setVisible(false);
            } else {
                if (Page.getCurrent() != null) {
                    ComponentErrorNotification.show(i18N.get("savable.save-exception"), validationStatus.getFieldValidationErrors());
                }
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean hasUnsavedData() {
        return hasChanges;
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
    protected void activateStep(WizardStep step) {
        super.activateStep(step);
        secondColumnLayout.removeAllComponents();
        ((LabWizardStep) step).placeAdditionalComponents(secondColumnLayout);
    }

    @Override
    public void finish() {
        binder.getBean().setCompleted(true);
        hasChanges = true;
        if (saveData()) {
            removeAllWindows();
            super.finish();
        }
        labFinishedWindow.show(binder.getBean(), labService);
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

    private void showInitialData() {
        initialDataWindow.show(binder.getBean().getVariant(), labService);
    }
}
