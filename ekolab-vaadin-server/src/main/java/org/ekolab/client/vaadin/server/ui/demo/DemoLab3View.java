package org.ekolab.client.vaadin.server.ui.demo;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Notification;
import org.ekolab.client.vaadin.server.ui.view.content.lab_3.Lab3View;
import org.ekolab.server.common.Profiles;
import org.springframework.context.annotation.Profile;

/**
 * Created by Андрей on 02.04.2017.
 */
@SpringView(name = DemoLab3View.NAME)
@Profile(value = {Profiles.MODE.DEMO, Profiles.MODE.DEV})
public class DemoLab3View extends Lab3View {
    @Override
    protected void beforeFinish() {
        Notification.show("Данная лабораторная работа не будет помечена как \"Выполненная\", т. к. вы находитесь в демо режиме", Notification.Type.WARNING_MESSAGE);
    }
}
