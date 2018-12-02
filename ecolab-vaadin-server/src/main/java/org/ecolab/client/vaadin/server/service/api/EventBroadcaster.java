package org.ecolab.client.vaadin.server.service.api;

import org.ecolab.client.vaadin.server.ui.VaadinUI;

public interface EventBroadcaster {
    void subscribe(VaadinUI ui);
    void unSubscribe(VaadinUI ui);
}
