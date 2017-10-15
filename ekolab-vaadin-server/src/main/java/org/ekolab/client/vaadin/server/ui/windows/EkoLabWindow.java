package org.ekolab.client.vaadin.server.ui.windows;

/**
 * Created by 777Al on 03.04.2017.
 */
public interface EkoLabWindow<T extends WindowSettings> {
    void show(T data);
}
