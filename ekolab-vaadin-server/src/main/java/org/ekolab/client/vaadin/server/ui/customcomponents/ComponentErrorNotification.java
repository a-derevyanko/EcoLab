package org.ekolab.client.vaadin.server.ui.customcomponents;

import com.vaadin.data.BindingValidationStatus;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Андрей on 22.10.2016.
 */
public class ComponentErrorNotification extends Notification {
    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentErrorNotification.class);

    public ComponentErrorNotification(String caption, String message) {
        super(caption, message, Type.WARNING_MESSAGE);
        setStyleName(EkoLabTheme.NOTIFICATION_TRAY);
        setPosition(Position.BOTTOM_CENTER);
    }

    public static void show(String caption, List<BindingValidationStatus<?>> validationStatuses) {
        String errors = IntStream.range(0, validationStatuses.size())
                .mapToObj(i -> (i + 1) + ") " + validationStatuses.get(i).getMessage().orElse(""))
                .collect(Collectors.joining("\n"));
        new ComponentErrorNotification(caption, errors).show(Page.getCurrent());
    }

    public static void show(String caption, String message) {
        new ComponentErrorNotification(caption, message).show(Page.getCurrent());
    }
}
