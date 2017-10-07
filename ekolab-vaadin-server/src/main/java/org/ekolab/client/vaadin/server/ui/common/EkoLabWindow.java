package org.ekolab.client.vaadin.server.ui.common;

/**
 * Created by 777Al on 03.04.2017.
 */
public interface EkoLabWindow<T extends EkoLabWindow.WindowSettings> {
    void show(T data);

    interface WindowSettings {

    }
}
