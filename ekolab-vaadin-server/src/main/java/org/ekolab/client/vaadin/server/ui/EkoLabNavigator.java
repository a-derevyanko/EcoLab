package org.ekolab.client.vaadin.server.ui;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.navigator.SpringNavigator;
import org.ekolab.client.vaadin.server.ui.view.LabChooserView;
import org.ekolab.client.vaadin.server.ui.view.LoginView;
import org.ekolab.server.common.Profiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.vaadin.spring.security.VaadinSecurity;

import javax.validation.constraints.NotNull;

/**
 * Created by 777Al on 23.11.2016.
 */
@UIScope
@SpringComponent
@Profile(Profiles.MODE.PROD)
public class EkoLabNavigator extends SpringNavigator {
    @Autowired
    protected VaadinSecurity vaadinSecurity;

    @Override
    public void navigateTo(String navigationState) {
        super.navigateTo(navigationState.isEmpty() ? LoginView.NAME : navigationState);
    }

    @NotNull
    @Override
    public String getState() {
        if (vaadinSecurity.isAuthenticated()) {
            return super.getState().isEmpty() ? LabChooserView.NAME: super.getState();
        } else {
            return LoginView.NAME;
        }
    }

}
