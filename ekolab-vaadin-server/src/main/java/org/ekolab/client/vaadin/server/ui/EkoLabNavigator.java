package org.ekolab.client.vaadin.server.ui;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.navigator.SpringNavigator;
import org.ekolab.client.vaadin.server.ui.view.LoginView;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.security.VaadinSecurity;

import javax.validation.constraints.NotNull;

/**
 * Created by 777Al on 23.11.2016.
 */
@UIScope
@SpringComponent
public class EkoLabNavigator extends SpringNavigator {
    @Autowired
    private VaadinSecurity vaadinSecurity;

    @NotNull
    @Override
    public String getState() {
        if (vaadinSecurity.isAuthenticated()) {
            return super.getState().isEmpty() ? ""/*RoomView.NAME */: super.getState();
        } else {
            return LoginView.NAME;
        }
    }

}
