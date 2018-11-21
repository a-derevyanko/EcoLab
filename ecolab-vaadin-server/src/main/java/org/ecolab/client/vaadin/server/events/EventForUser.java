package org.ecolab.client.vaadin.server.events;

/**
 * Событие, которое предназначается пользователю
 */
public interface EventForUser extends Event {
    long getUserId();
}
