package org.ekolab.client.vaadin.server.ui.view.content.lab_2;

import org.ekolab.client.vaadin.server.ui.common.LabWizard;
import org.ekolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ekolab.client.vaadin.server.ui.view.api.AutoSavableView;

import java.util.Collection;

/**
 * Created by Андрей on 02.04.2017.
 */
public class Lab2View extends LabWizard implements AutoSavableView {
    public Lab2View() {
        super(null, null, null);
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    protected Collection<LabWizardStep> getLabSteps() {
        return null;
    }
}
