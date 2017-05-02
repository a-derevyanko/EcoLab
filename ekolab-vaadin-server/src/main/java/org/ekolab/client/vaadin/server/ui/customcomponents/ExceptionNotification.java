package org.ekolab.client.vaadin.server.ui.customcomponents;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Notification;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.server.common.Profiles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;

/**
 * Created by Андрей on 22.10.2016.
 */
@SpringComponent
@UIScope
@Profile(Profiles.MODE.PROD)
public class ExceptionNotification extends Notification {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionNotification.class);

    @Autowired
    public ExceptionNotification(I18N i18N) {
        super(i18N.get("exception.title"), Type.TRAY_NOTIFICATION);
        setStyleName(EkoLabTheme.NOTIFICATION_CLOSABLE);
        setPosition(Position.BOTTOM_CENTER);
    }

    public void show(Page page, Throwable ex) {
        Throwable rootEx = ExceptionUtils.getRootCause(ex);
        LOGGER.error(ex.getLocalizedMessage(), rootEx == null ? ex : rootEx);
        super.show(page);
    }

    public void show(Throwable ex) {
        show(Page.getCurrent(), ex);
    }
}
