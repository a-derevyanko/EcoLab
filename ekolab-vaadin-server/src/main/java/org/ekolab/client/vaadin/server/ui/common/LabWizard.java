package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.ui.Alignment;
import org.vaadin.teemu.wizards.Wizard;

/**
 * Created by Андрей on 19.03.2017.
 */
public class LabWizard extends Wizard {
    public LabWizard() {
        /*getBackButton().setCaption("");
        getCancelButton().setCaption("");
        getNextButton().setCaption("");
        getFinishButton().setCaption("");*/
        mainLayout.setComponentAlignment(footer, Alignment.BOTTOM_CENTER);
    }
}
