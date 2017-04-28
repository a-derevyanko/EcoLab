package org.ekolab.client.vaadin.server.ui.view.content.lab_2;

import org.ekolab.client.vaadin.server.ui.common.LabWizard;
import org.ekolab.client.vaadin.server.ui.view.api.AutoSavableView;
import org.ekolab.server.model.LabData;

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
    protected LabData createNewLabData() {
        return null;
    }
}
