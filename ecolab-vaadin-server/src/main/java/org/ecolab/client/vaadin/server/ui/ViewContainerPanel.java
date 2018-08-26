package org.ecolab.client.vaadin.server.ui;

import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Panel;

import javax.annotation.PostConstruct;

/**
 * Created by 777Al on 23.11.2016.
 */
@SpringViewDisplay
public class ViewContainerPanel extends Panel {
    @PostConstruct
    public void init() {
        setSizeFull();
    }
}
