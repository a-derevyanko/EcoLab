package org.ecolab.client.vaadin.server.events;

public class LabAllowanceChangedEvent extends BaseUserEvent {
    public LabAllowanceChangedEvent(long userId) {
        super(userId);
    }
}
