package org.ecolab.client.vaadin.server.ui.windows;

import java.io.Serializable;

/**
 * Created by 777Al on 03.04.2017.
 */
public interface EcoLabWindow<T extends WindowSettings> extends Serializable {
    void show(T data);
}
