package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Created by 777Al on 20.04.2017.
 */
public class ResourceWindow extends Window {
    private final VerticalLayout content = new VerticalLayout();

    public ResourceWindow(String fieldName, BrowserFrame infoResource) {
        super(fieldName);
        setCaptionAsHtml(true);
        setContent(content);
        setHeight(50.0F, Unit.PERCENTAGE);
        setWidth(50.0F, Unit.PERCENTAGE);
        content.setSizeFull();
        content.addComponent(infoResource);
        infoResource.setSizeFull();
        center();
    }

    public static void show(String fieldName, BrowserFrame infoResource) {
        UI.getCurrent().addWindow(new ResourceWindow(fieldName, infoResource));
    }
}
