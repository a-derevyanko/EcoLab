package org.ecolab.server.model;

import org.aderevyanko.audit.api.AuditEventType;
import org.aderevyanko.audit.api.generic.GenericAuditEvent;

public class EcoLabAuditEvent extends GenericAuditEvent< EcoLabAuditEvent> {
    /**
     * Получатель события системы
     */
    private Long eventConsumerId;

    private EcoLabAuditEvent(AuditEventType eventType) {
        super(eventType);
    }

    /**
     * Static constructor method
     * @param eventType type of an event
     * @return this event for chain builder
     */
    public static EcoLabAuditEvent ofType(AuditEventType eventType) {
        return new EcoLabAuditEvent(eventType);
    }

    /**
     * Назначение получателя события
     * @param consumerId получатель
     * @return события
     */
    public EcoLabAuditEvent forUser(Long consumerId) {
        eventConsumerId = consumerId;
        return this;
    }

    public Long getEventConsumerId() {
        return eventConsumerId;
    }
}
