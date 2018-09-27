package org.ecolab.server.model;

import org.aderevyanko.audit.api.AuditEventType;
import org.aderevyanko.audit.api.generic.GenericAuditEvent;

public class EcoLabAuditEvent extends GenericAuditEvent< EcoLabAuditEvent> {
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
}
