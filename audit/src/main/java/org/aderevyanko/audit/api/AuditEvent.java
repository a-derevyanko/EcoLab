package org.aderevyanko.audit.api;

import org.aderevyanko.audit.api.base.BaseAuditEvent;

public class AuditEvent extends BaseAuditEvent<AuditEventHeader, AuditEvent> {
    private AuditEvent(AuditEventType eventType) {
        super(new AuditEventHeader(), eventType);
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
