package org.ekolab.client.vaadin.server.ui.customcomponents;

import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;
import org.ekolab.client.vaadin.server.service.I18N;

/**
 * Created by Андрей on 22.10.2016.
 */
@SpringComponent
@UIScope
public class ExceptionNotification extends Notification {
    private final I18N i18N;

    public ExceptionNotification(I18N i18N) {
        super(i18N.get("exception.title"), Type.TRAY_NOTIFICATION);
        this.i18N = i18N;
        setStyleName(ValoTheme.NOTIFICATION_CLOSABLE);
        setPosition(Position.BOTTOM_CENTER);
    }
}
