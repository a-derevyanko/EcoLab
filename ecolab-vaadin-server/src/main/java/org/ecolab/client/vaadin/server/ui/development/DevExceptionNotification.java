package org.ecolab.client.vaadin.server.ui.development;

import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.customcomponents.ExceptionNotification;
import org.ecolab.client.vaadin.server.ui.styles.EcoLabTheme;
import org.ecolab.server.common.Profiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

/**
 * Created by Андрей on 22.10.2016.
 */
@SpringComponent
@UIScope
@Profile(Profiles.MODE.DEV)
public class DevExceptionNotification extends ExceptionNotification {
    @Autowired
    public DevExceptionNotification(I18N i18N) {
        super(i18N);
        setStyleName(EcoLabTheme.NOTIFICATION_ERROR);
        setDelayMsec(-1);
    }

    @Override
    public void show(Page page, Throwable ex) {
        setDescription(Arrays.toString(ex.getStackTrace()));
        super.show(page, ex);
    }
}
