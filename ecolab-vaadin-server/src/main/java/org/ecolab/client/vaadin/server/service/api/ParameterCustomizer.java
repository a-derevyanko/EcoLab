package org.ecolab.client.vaadin.server.service.api;

import java.io.Serializable;

/**
 * Created by 777Al on 30.05.2017.
 */
public interface ParameterCustomizer extends Serializable {
    /**
     * Возвращает префикс параметру, добавляемому на форму.
     * @return префикс параметра
     */
    String getParameterPrefix();
}
