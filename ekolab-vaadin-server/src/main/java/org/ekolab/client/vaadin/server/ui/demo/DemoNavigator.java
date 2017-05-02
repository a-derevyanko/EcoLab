package org.ekolab.client.vaadin.server.ui.demo;

import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.ekolab.client.vaadin.server.ui.EkoLabNavigator;
import org.ekolab.client.vaadin.server.ui.view.LabChooserView;
import org.ekolab.server.common.Profiles;
import org.springframework.context.annotation.Profile;

/**
 * Навигатор без редиректа на главную.
 */
@UIScope
@SpringComponent
@Profile(Profiles.MODE.DEV)
public class DemoNavigator extends EkoLabNavigator {
    @Override
    public void navigateTo(String navigationState) {
        if (VaadinSession.getCurrent().getState() == VaadinSession.State.OPEN) {
            super.navigateTo(navigationState.isEmpty() ? LabChooserView.NAME : navigationState);
        }
    }

    @Override
    public String getState() {
        return getStateManager().getState().isEmpty() ? LabChooserView.NAME: getStateManager().getState();
    }
}
