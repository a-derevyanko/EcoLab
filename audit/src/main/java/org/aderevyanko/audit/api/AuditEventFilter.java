package org.aderevyanko.audit.api;

import org.aderevyanko.audit.api.base.BaseAuditEventFilter;

public class AuditEventFilter extends BaseAuditEventFilter<AuditEventHeader> {
    private AuditEventFilter() {
        super(new AuditEventHeader());
    }

    /**
     * Static constructor method
     * @param eventType type of an event. Can be {@code null} if all event types needed.
     * @return this event for chain builder
     */
    public static AuditEventFilter of(AuditEventType eventType) {
        AuditEventFilter filter = new AuditEventFilter();
        filter.header.setEventType(eventType);
        return filter;
    }

}
