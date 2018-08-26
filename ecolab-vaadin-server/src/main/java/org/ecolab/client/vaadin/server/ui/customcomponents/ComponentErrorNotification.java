package org.ecolab.client.vaadin.server.ui.customcomponents;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import org.ecolab.client.vaadin.server.ui.styles.EcoLabTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Андрей on 22.10.2016.
 */
public class ComponentErrorNotification extends Notification {
    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentErrorNotification.class);

    private ComponentErrorNotification(String caption, String message) {
        super(caption, message, Type.WARNING_MESSAGE);
        setStyleName(EcoLabTheme.NOTIFICATION_TRAY);
        setPosition(Position.BOTTOM_CENTER);
    }

    public static void show(String caption, String message) {
        new ComponentErrorNotification(caption, message).show(Page.getCurrent());
    }
}
