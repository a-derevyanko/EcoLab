package org.ekolab.client.vaadin.server.ui;

import com.vaadin.navigator.NavigationStateManager;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.ui.UI;
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
    private VaadinSecurity vaadinSecurity;
    @Autowired
    private ViewProvider viewProvider;

    @Override
    public void init(UI ui, NavigationStateManager stateManager, ViewDisplay display) {
        super.init(ui, stateManager, display);
        addView("", viewProvider.getView(LoginView.NAME));
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
