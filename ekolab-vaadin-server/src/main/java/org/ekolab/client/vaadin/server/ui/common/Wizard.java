package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.UI;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.client.vaadin.server.ui.view.api.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardProgressListener;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

public abstract class Wizard extends org.vaadin.teemu.wizards.Wizard implements View, WizardProgressListener {
    // ---------------------------- Графические компоненты --------------------

    protected final I18N i18N;

    @Autowired
    public Wizard(I18N i18N) {
        this.i18N = i18N;
    }

    @PostConstruct
    public void init() throws Exception {
        View.super.init();
        mainLayout.setSizeFull();
        getCancelButton().setVisible(false);
        getFinishButton().setVisible(false);

        addStyleName(EkoLabTheme.PANEL_WIZARD);
        getFinishButton().addStyleName(EkoLabTheme.BUTTON_PRIMARY);
        getFinishButton().addStyleName(EkoLabTheme.BUTTON_TINY);
        getNextButton().addStyleName(EkoLabTheme.BUTTON_PRIMARY);
        getNextButton().addStyleName(EkoLabTheme.BUTTON_TINY);
        getBackButton().addStyleName(EkoLabTheme.BUTTON_PRIMARY);
        getBackButton().addStyleName(EkoLabTheme.BUTTON_TINY);

        getBackButton().setCaption(i18N.get("labwizard.back"));
        getNextButton().setCaption(i18N.get("labwizard.next"));
        getFinishButton().setCaption(i18N.get("labwizard.finish"));

        getFinishButton().setIcon(VaadinIcons.FLAG_CHECKERED, i18N.get("labwizard.finish"));
        getNextButton().setIcon(VaadinIcons.ARROW_FORWARD, i18N.get("labwizard.next"));
        getBackButton().setIcon(VaadinIcons.ARROW_BACKWARD, i18N.get("labwizard.back"));
        addListener(this);

        footer.removeComponent(getCancelButton());

        replaceHeader();
    }

    @Override
    public void addStep(WizardStep step, String id) {
        super.addStep(step, id);
        updateButtons();
    }

    @Override
    protected void activateStep(WizardStep step) {
        super.activateStep(step);
    }

    @Override
    public void activeStepChanged(WizardStepActivationEvent event) {
        updateButtons();
    }

    @Override
    public void stepSetChanged(WizardStepSetChangedEvent event) {
        updateButtons();
    }

    @Override
    public void wizardCancelled(WizardCancelledEvent event) {
        removeAllWindows();
    }

    protected void replaceHeader() {
        removeListener((WizardProgressListener) getHeader());
        WizardProgressBar progressBar = new WizardProgressBar(this);
        addListener(progressBar);
        setHeader(progressBar);
    }

    protected void removeAllWindows() {
        new ArrayList<>(UI.getCurrent().getWindows()).forEach(UI.getCurrent()::removeWindow);
    }

    protected void updateButtons() {
        boolean lastStep = isLastStep(currentStep);
        getFinishButton().setVisible(lastStep);
        getNextButton().setVisible(!lastStep);
        getBackButton().setVisible(!isFirstStep(currentStep));
    }
}
