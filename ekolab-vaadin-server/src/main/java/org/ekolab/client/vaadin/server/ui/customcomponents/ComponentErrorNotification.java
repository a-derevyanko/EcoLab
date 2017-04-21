package org.ekolab.client.vaadin.server.ui.customcomponents;

import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Андрей on 22.10.2016.
 */
public class ComponentErrorNotification extends Notification {
    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentErrorNotification.class);

    public ComponentErrorNotification(String message) {
        super(null, message, Type.WARNING_MESSAGE, true);
        setStyleName(EkoLabTheme.NOTIFICATION_CLOSABLE);
        setPosition(Position.BOTTOM_CENTER);
    }

    public static void show(ErrorMessage errorMessage) {
        new ComponentErrorNotification(errorMessage.getFormattedHtmlMessage()).show(Page.getCurrent());
    }
}
