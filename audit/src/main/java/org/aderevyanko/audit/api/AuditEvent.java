package org.aderevyanko.audit.api;

import org.aderevyanko.audit.api.generic.GenericAuditEvent;

public class AuditEvent extends GenericAuditEvent<AuditEvent> {
    private AuditEvent(AuditEventType eventType) {
        super(eventType);
    }

    /**
     * Static constructor method
     * @param eventType type of an event
     * @return this event for chain builder
     */
    public static AuditEvent ofType(AuditEventType eventType) {
        return new AuditEvent(eventType);
    }
}
