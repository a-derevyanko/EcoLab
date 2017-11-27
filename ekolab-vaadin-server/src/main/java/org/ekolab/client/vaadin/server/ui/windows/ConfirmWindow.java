package org.ekolab.client.vaadin.server.ui.windows;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.UI;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.vaadin.dialogs.ConfirmDialog;

@SpringComponent
@UIScope
public class ConfirmWindow implements EkoLabWindow<ConfirmWindow.ConfirmWindowSettings> {
    private final I18N i18N;

    public ConfirmWindow(I18N i18N) {
        this.i18N = i18N;
    }

    @Override
    public void show(ConfirmWindowSettings data) {
        ConfirmDialog.show(UI.getCurrent(), i18N.get("confirm.title"),
                i18N.get(data.messageKey), i18N.get("confirm.ok"),
                i18N.get("confirm.cancel"), data.action);
    }

    public static class ConfirmWindowSettings implements WindowSettings {
        private final String messageKey;
        private final Runnable action;

        public ConfirmWindowSettings(String messageKey, Runnable action) {
            this.messageKey = messageKey;
            this.action = action;
        }
    }
}
