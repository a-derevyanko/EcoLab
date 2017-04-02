package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Alignment;
import org.ekolab.client.vaadin.server.ui.view.SavableView;
import org.vaadin.teemu.wizards.Wizard;

/**
 * Created by Андрей on 19.03.2017.
 */
public abstract class LabWizard extends Wizard implements SavableView {
    public LabWizard() {
        /*getBackButton().setCaption("");
        getCancelButton().setCaption("");
        getNextButton().setCaption("");
        getFinishButton().setCaption("");*/
        mainLayout.setComponentAlignment(footer, Alignment.BOTTOM_CENTER);
    }
    @Override
    //todo может сделать его необязательным?
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}
