package org.ekolab.client.vaadin.server.ui;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.spring.navigator.SpringNavigator;
import com.vaadin.ui.UI;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.view.LabChooserView;
import org.ekolab.client.vaadin.server.ui.view.LoginView;
import org.ekolab.client.vaadin.server.ui.view.api.SavableView;
import org.ekolab.server.common.Profiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.spring.security.VaadinSecurity;

import javax.validation.constraints.NotNull;

/**
 * Created by 777Al on 23.11.2016.
 */
@UIScope
@SpringComponent
@Profile(value = {Profiles.MODE.PROD, Profiles.MODE.DEMO})
public class EkoLabNavigator extends SpringNavigator {
    @Autowired
    protected I18N i18N;

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

    public void redirectToView(String viewName) {
        if (getCurrentView() instanceof SavableView && ((SavableView) getCurrentView()).hasUnsavedData()) {
            ConfirmDialog.show(UI.getCurrent(), i18N.get("confirm.title"),
                    i18N.get("savable.unsaved-data-missing"), i18N.get("confirm.ok"),
                    i18N.get("confirm.cancel"), (ConfirmDialog.Listener) dialog -> {
                        if (!dialog.isCanceled() && dialog.isConfirmed()) {
                            EkoLabNavigator.super.navigateTo(viewName);
                        }
                    });
        } else {
            super.navigateTo(viewName);
        }
    }
}
