package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.VerticalLayout;
import org.springframework.util.StringUtils;
import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardProgressListener;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WizardProgressBar extends CustomComponent implements
        WizardProgressListener {

    private final Wizard wizard;
    private final ProgressBar progressBar = new ProgressBar();
    private final HorizontalLayout stepCaptions = new HorizontalLayout();
    private final Map<Label, WizardStep> captionsWithSteps = new HashMap<>();
    private int activeStepIndex;

    public WizardProgressBar(Wizard wizard) {
        setStyleName("wizard-progress-bar");
        this.wizard = wizard;

        stepCaptions.setWidth(100, Unit.PERCENTAGE);
        progressBar.setWidth(100, Unit.PERCENTAGE);
        progressBar.setHeight(13, Unit.PIXELS);

        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("100%");
        layout.addComponent(stepCaptions);
        layout.addComponent(progressBar);
        setCompositionRoot(layout);
        setWidth(100, Unit.PERCENTAGE);

        stepCaptions.addLayoutClickListener(event ->
                wizard.activateStep(captionsWithSteps.get(event.getClickedComponent()))
        );
    }

    private void updateProgressBar() {
        int stepCount = wizard.getSteps().size();
        float padding = (1.0f / stepCount) / 2;
        float progressValue = padding + activeStepIndex / (float) stepCount;
        progressBar.setValue(progressValue);
    }

    private void updateStepCaptions() {
        stepCaptions.removeAllComponents();
        int index = 1;
        for (WizardStep step : wizard.getSteps()) {
            Label label = createCaptionLabel(index, step);
            stepCaptions.addComponent(label);
            index++;
        }
    }

    private Label createCaptionLabel(int index, WizardStep step) {
        Label label = new Label(StringUtils.hasText(step.getCaption()) ? index + ". " + step.getCaption() : String.valueOf(index));
        label.setSizeFull();
        label.addStyleName("step-caption");

        List<WizardStep> steps = wizard.getSteps();
        if (wizard.isCompleted(step)) {
            label.addStyleName("completed");
        }
        if (wizard.isActive(step)) {
            label.addStyleName("current");
        }
        if (steps.indexOf(step) == 0) {
            label.addStyleName("first");
        }
        if (steps.indexOf(step) == (steps.size() - 1)) {
            label.addStyleName("last");
        }

        captionsWithSteps.put(label, step);
        return label;
    }

    private void updateProgressAndCaptions() {
        updateProgressBar();
        updateStepCaptions();
    }

    @Override
    public void activeStepChanged(WizardStepActivationEvent event) {
        List<WizardStep> allSteps = wizard.getSteps();
        activeStepIndex = allSteps.indexOf(event.getActivatedStep());
        updateProgressAndCaptions();
    }

    @Override
    public void stepSetChanged(WizardStepSetChangedEvent event) {
        updateProgressAndCaptions();
    }

    @Override
    public void wizardCompleted(WizardCompletedEvent event) {
        progressBar.setValue(1.0f);
        updateStepCaptions();
    }

    @Override
    public void wizardCancelled(WizardCancelledEvent event) {
        // NOP, no need to react to cancellation
    }
}
