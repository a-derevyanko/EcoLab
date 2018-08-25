package org.ecolab.client.vaadin.server.ui;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.ui.UI;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.view.AdminManagingView;
import org.ecolab.client.vaadin.server.ui.view.LabChooserView;
import org.ecolab.client.vaadin.server.ui.view.LoginView;
import org.ecolab.client.vaadin.server.ui.view.TeacherGroupsManagingView;
import org.ecolab.client.vaadin.server.ui.view.api.SavableView;
import org.ecolab.client.vaadin.server.ui.windows.ConfirmWindow;
import org.ecolab.server.common.Profiles;
import org.ecolab.server.common.Role;
import org.ecolab.server.common.UserInfoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.vaadin.spring.security.VaadinSecurity;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by 777Al on 23.11.2016.
 */
@UIScope
@SpringComponent
@Profile(value = {Profiles.MODE.PROD, Profiles.MODE.DEMO})
public class EcoLabNavigator extends SpringNavigator {
    protected final I18N i18N;

    protected final VaadinSecurity vaadinSecurity;

    protected final ConfirmWindow confirmWindow;

    @Autowired
    public EcoLabNavigator(I18N i18N, VaadinSecurity vaadinSecurity, ConfirmWindow confirmWindow) {
        this.i18N = i18N;
        this.vaadinSecurity = vaadinSecurity;
        this.confirmWindow = confirmWindow;
    }

    @Override
    public void navigateTo(String navigationState) {
        super.navigateTo(navigationState.isEmpty() ? LoginView.NAME : navigationState);
    }

    @NotNull
    @Override
    public String getState() {
        if (vaadinSecurity.isAuthenticated()) {
            if (super.getState().isEmpty()) {
                Set<String> roles = UserInfoUtils.getRoles(vaadinSecurity.getAuthentication());

                if (roles.contains(Role.ADMIN)) {
                    return AdminManagingView.NAME;
                } else if (roles.contains(Role.TEACHER)) {
                    return TeacherGroupsManagingView.NAME;
                } else {
                    return LabChooserView.NAME;
                }
            } else {
                return super.getState();
            }
        } else {
            return LoginView.NAME;
        }
    }

    public void redirectToView(String viewName) {
        new ArrayList<>(UI.getCurrent().getWindows()).forEach(UI.getCurrent()::removeWindow);
        if (getCurrentView() instanceof SavableView && ((SavableView) getCurrentView()).hasUnsavedData()) {
            confirmWindow.show(new ConfirmWindow.ConfirmWindowSettings("savable.unsaved-data-missing",
            () -> EcoLabNavigator.super.navigateTo(viewName)));
        } else {
            super.navigateTo(viewName);
        }
    }
}
