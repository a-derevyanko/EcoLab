package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.client.vaadin.server.ui.view.api.View;
import org.vaadin.teemu.wizards.WizardStep;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Created by 777Al on 05.04.2017.
 */
public interface LabWizardStep extends Component, View, WizardStep {
    @Override
    @PostConstruct
    default void init() throws IOException {
        setSizeUndefined();
        setStyleName(EkoLabTheme.PANEL_WIZARD_STEP);
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

    default void placeAdditionalButtons(HorizontalLayout buttonsLayout) {}
}
