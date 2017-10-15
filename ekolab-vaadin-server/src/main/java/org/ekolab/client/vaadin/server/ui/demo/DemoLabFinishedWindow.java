package org.ekolab.client.vaadin.server.ui.demo;

import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Notification;
import org.ekolab.client.vaadin.server.ui.windows.LabFinishedWindow;
import org.ekolab.server.common.Profiles;
import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.LabVariant;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

/**
 * Created by 777Al on 20.04.2017.
 */
@SpringComponent
@UIScope
@Profile(value = {Profiles.MODE.DEV})
public class DemoLabFinishedWindow<T extends LabData<V>, V extends LabVariant> extends LabFinishedWindow<T, V> implements Action.Handler{

    // ---------------------------- Графические компоненты --------------------
    private final Action saveAction = new ShortcutAction("SaveData",
            ShortcutAction.KeyCode.S, new int[] {ShortcutAction.ModifierKey.ALT});

    @PostConstruct
    public void init() {
        super.init();
        addActionHandler(this);
    }

    @Override
    protected void beforeShow() {
        super.beforeShow();
        Notification notification = new Notification("Данная лабораторная работа " +
                "будет автоматически помечена как \"Выполненная\", т. к. вы закончили её выполнение. В режиме " +
                "разработчика вы можете сбросить данный признак, для этого нажмите ALT+S", Notification.Type.WARNING_MESSAGE);
        notification.setDelayMsec(10000);
        notification.show(Page.getCurrent());
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
