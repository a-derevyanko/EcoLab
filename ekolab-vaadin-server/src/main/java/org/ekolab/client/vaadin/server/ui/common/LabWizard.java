package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.customcomponents.ComponentErrorNotification;
import org.ekolab.client.vaadin.server.ui.customcomponents.ExceptionNotification;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.client.vaadin.server.ui.view.api.AutoSavableView;
import org.ekolab.server.model.LabData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.WizardStep;

/**
 * Created by Андрей on 19.03.2017.
 */
public abstract class LabWizard<BEAN extends LabData> extends Wizard implements AutoSavableView {
    @Autowired
    private I18N i18N;

    @Autowired
    private ExceptionNotification exceptionNotification;

    // ---------------------------- Графические компоненты --------------------
    protected final GridLayout buttons = new GridLayout(3, 1);

    protected final HorizontalLayout additionalComponentsLayout = new HorizontalLayout();

    @Override
    public void init() throws Exception {
        AutoSavableView.super.init();
        mainLayout.setSizeFull();
        buttons.setSizeFull();
        buttons.setHeightUndefined();
        buttons.setSpacing(true);
        getHeader().setVisible(false);
        getCancelButton().setVisible(false);
        getFinishButton().setVisible(false);

        addStyleName(EkoLabTheme.PANEL_WIZARD);
        getFinishButton().addStyleName(EkoLabTheme.BUTTON_PRIMARY);
        getNextButton().addStyleName(EkoLabTheme.BUTTON_PRIMARY);
        getBackButton().addStyleName(EkoLabTheme.BUTTON_PRIMARY);

        getBackButton().setCaption(i18N.get("labwizard.back"));
        getNextButton().setCaption(i18N.get("labwizard.next"));
        getFinishButton().setCaption(i18N.get("labwizard.finish"));

        getFinishButton().setIcon(VaadinIcons.FLAG_CHECKERED, i18N.get("labwizard.finish"));
        getNextButton().setIcon(VaadinIcons.ARROW_FORWARD, i18N.get("labwizard.next"));
        getBackButton().setIcon(VaadinIcons.ARROW_BACKWARD, i18N.get("labwizard.back"));

        getBackButton().addClickListener(event -> saveData());
        getNextButton().addClickListener(event -> saveData());
        getFinishButton().addClickListener(event -> saveData());

        footer.removeComponent(getCancelButton());
        footer.removeComponent(getBackButton());
        mainLayout.removeComponent(footer);

        mainLayout.addComponent(buttons);

        buttons.setColumnExpandRatio(1, 1.0F);
        buttons.addComponent(getBackButton(), 0, 0);
        buttons.addComponent(additionalComponentsLayout, 1, 0);
        buttons.addComponent(footer, 2, 0);

        buttons.setComponentAlignment(additionalComponentsLayout, Alignment.MIDDLE_CENTER);
    }

    @Override
    @Scheduled(fixedRateString = "${lab.autoSaveRate:#{60000}}", initialDelayString = "${lab.autoSaveRate:#{60000}}")
    public void autoSave() {
        saveData();
    }

    @Override
    public void saveData() {
        if (getComponentError() == null) {
            /*try {
                getBinder().writeBean(null);
            } catch (ValidationException e) {
                exceptionNotification.show(e);
            }*/
        } else {
            ComponentErrorNotification.show(getComponentError());
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
        removeAllWindows();
        super.finish();
    }

    @Override
    public void next() {
        removeAllWindows();
        super.next();
    }

    @Override
    public void back() {
        removeAllWindows();
        super.back();
    }

    protected abstract Binder<BEAN> getBinder();

    private void updateButtons() {
        boolean lastStep = isLastStep(currentStep);
        getFinishButton().setVisible(lastStep);
        getNextButton().setVisible(!lastStep);
        getBackButton().setVisible(!isFirstStep(currentStep));
    }

    private void removeAllWindows() {
        UI.getCurrent().getWindows().forEach(UI.getCurrent()::removeWindow);
    }
}
