package org.ecolab.client.vaadin.server.ui.development;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import org.ecolab.client.vaadin.server.service.api.EventBroadcaster;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.EcoLabMenuBar;
import org.ecolab.client.vaadin.server.ui.EcoLabNavigator;
import org.ecolab.client.vaadin.server.ui.VaadinUI;
import org.ecolab.client.vaadin.server.ui.ViewContainerPanel;
import org.ecolab.client.vaadin.server.ui.customcomponents.ExceptionNotification;
import org.ecolab.server.common.Profiles;
import org.springframework.context.annotation.Profile;
import org.vaadin.spring.security.shared.VaadinSharedSecurity;

/**
 * Created by Андрей on 04.09.2016.
 */
@SpringUI
@Profile({Profiles.MODE.DEV})
public class DevVaadinUI extends VaadinUI {
    public DevVaadinUI(EventBroadcaster eventBroadcaster, EcoLabNavigator navigator, EcoLabMenuBar menuBar,
                       ExceptionNotification exceptionNotification, ViewContainerPanel viewContainer,
                       VaadinSharedSecurity vaadinSecurity, I18N i18N) {
        super(eventBroadcaster, navigator, menuBar, exceptionNotification, viewContainer, vaadinSecurity, i18N);
    }

    @Override
    protected void init(VaadinRequest request) {
        DevUtils.authenticateAsAdmin(vaadinSecurity);
        super.init(request);
    }
}