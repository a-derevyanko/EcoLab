package org.ecolab.client.vaadin.server.events;

/**
 * Событие, которое предназначается пользователю
 */
abstract class BaseUserEvent implements EventForUser {
    private final long userId;

    BaseUserEvent(long userId) {
        this.userId = userId;
    }

    @Override
    public long getUserId() {
        return userId;
    }
}
