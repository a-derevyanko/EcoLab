package org.ekolab.client.vaadin.server.ui.demo;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import org.ekolab.client.vaadin.server.ui.VaadinUI;
import org.ekolab.server.common.Profiles;
import org.springframework.context.annotation.Profile;

/**
 * Created by Андрей on 04.09.2016.
 */
@SpringUI
@Profile(Profiles.MODE.DEV)
public class DemoVaadinUI extends VaadinUI {
    @Override
    protected void init(VaadinRequest request) {
        DemoUtils.authenticateAsAdmin(vaadinSecurity);
        super.init(request);
    }
}