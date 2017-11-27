package org.ekolab.client.vaadin.server.ui.development;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.EkoLabMenuBar;
import org.ekolab.client.vaadin.server.ui.EkoLabNavigator;
import org.ekolab.client.vaadin.server.ui.VaadinUI;
import org.ekolab.client.vaadin.server.ui.ViewContainerPanel;
import org.ekolab.client.vaadin.server.ui.customcomponents.ExceptionNotification;
import org.ekolab.server.common.Profiles;
import org.springframework.context.annotation.Profile;
import org.vaadin.spring.security.shared.VaadinSharedSecurity;

/**
 * Created by Андрей on 04.09.2016.
 */
@SpringUI
@Profile({Profiles.MODE.DEV})
public class DevVaadinUI extends VaadinUI {
    public DevVaadinUI(EkoLabNavigator navigator, EkoLabMenuBar menuBar, ExceptionNotification exceptionNotification, ViewContainerPanel viewContainer, VaadinSharedSecurity vaadinSecurity, I18N i18N) {
        super(navigator, menuBar, exceptionNotification, viewContainer, vaadinSecurity, i18N);
    }

    @Override
    protected void init(VaadinRequest request) {
        DevUtils.authenticateAsAdmin(vaadinSecurity);
        super.init(request);
    }
}