package org.ecolab.client.vaadin.server.service.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.ecolab.server.common.EcoLabAuditEventType;

/**
 * Метод, помеченный данной аннотацией является слушателем событий
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UIEventListener {
    /**
     * Событие системы, на которое подписывается метод
     * @return тип события системы
     */
    EcoLabAuditEventType[] value();
}
