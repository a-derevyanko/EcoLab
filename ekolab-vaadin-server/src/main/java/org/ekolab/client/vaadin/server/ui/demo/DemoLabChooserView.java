package org.ekolab.client.vaadin.server.ui.demo;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.NativeButton;
import org.ekolab.client.vaadin.server.ui.view.LabChooserView;
import org.ekolab.server.common.Profiles;
import org.springframework.context.annotation.Profile;

/**
 * Created by 777Al on 03.04.2017.
 */
@SpringView(name = LabChooserView.NAME)
@Profile(value = {Profiles.MODE.DEV})
public class DemoLabChooserView extends LabChooserView {
    @Override
    protected void setButtonSate(NativeButton button, boolean enabled) {
    }
}
