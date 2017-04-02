package org.ekolab.client.vaadin.server.ui.demo;

import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.themes.ValoTheme;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.customcomponents.ExceptionNotification;
import org.ekolab.server.common.Profiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

/**
 * Created by Андрей on 22.10.2016.
 */
@SpringComponent
@UIScope
@Profile(Profiles.MODE.DEV)
public class DemoExceptionNotification extends ExceptionNotification {
    @Autowired
    public DemoExceptionNotification(I18N i18N) {
        super(i18N);
        setStyleName(ValoTheme.NOTIFICATION_ERROR);
        setDelayMsec(-1);
    }

    @Override
    public void show(Page page, Throwable ex) {
        setDescription(Arrays.toString(ex.getStackTrace()));
        super.show(page);
    }
}
