package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.ui.Component;
import org.ekolab.client.vaadin.server.ui.view.api.AutoSavableView;
import org.vaadin.teemu.wizards.WizardStep;

/**
 * Created by 777Al on 05.04.2017.
 */
public interface LabWizardStep extends Component, AutoSavableView, WizardStep {
    @Override
    default String getCaption() {
        return this.getClass().getSimpleName();
    }

    @Override
    default Component getContent() {
        return this;
    }

    @Override
    default boolean onAdvance() {
        return true;
    }

    @Override
    default boolean onBack() {
        return true;
    }
}
