package org.ecolab.client.vaadin.server.ui.demo;

import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Notification;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.EcoLabNavigator;
import org.ecolab.client.vaadin.server.ui.windows.LabFinishedWindow;
import org.ecolab.server.common.Profiles;
import org.ecolab.server.model.content.LabData;
import org.ecolab.server.model.content.LabVariant;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

/**
 * Created by 777Al on 20.04.2017.
 */
@SpringComponent
@UIScope
@Profile(value = {Profiles.MODE.DEMO})
public class DemoLabFinishedWindow<T extends LabData<V>, V extends LabVariant> extends LabFinishedWindow<T, V> implements Action.Handler{

    // ---------------------------- Графические компоненты --------------------
    private final Action saveAction = new ShortcutAction("SaveData",
            ShortcutAction.KeyCode.S, new int[] {ShortcutAction.ModifierKey.ALT});

    public DemoLabFinishedWindow(I18N i18N, EcoLabNavigator navigator) {
        super(i18N, navigator);
    }

    @PostConstruct
    public void init() {
        super.init();
        addActionHandler(this);
    }

    @Override
    protected void beforeShow() {
        super.beforeShow();
        Notification.show("Данная лабораторная работа " +
                "будет автоматически помечена\nкак \"Выполненная\", т. к. вы закончили её выполнение. В демонстрационно режиме\n" +
                "вы можете сбросить данный признак, для этого нажмите ALT+S", Notification.Type.TRAY_NOTIFICATION);
    }

    @Override
    public Action[] getActions(Object target, Object sender) {
        return new Action[] {saveAction};
    }

    @Override
    public void handleAction(Action action, Object sender, Object target) {
        if (action == saveAction) {
            settings.getLabData().setCompleted(false);
            settings.getLabService().updateLab(settings.getLabData());
        }
    }
}
