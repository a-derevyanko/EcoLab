package org.aderevyanko.audit.api;

import org.aderevyanko.audit.api.generic.GenericAuditEventFilter;

import java.util.Arrays;
import java.util.List;

public class AuditEventFilter extends GenericAuditEventFilter<AuditEventFilter> {
    private AuditEventFilter(List<AuditEventType> types) {
        super(types);
    }

    /**
     * Static constructor method
     * @param eventTypes types of events. Can be empty if all event types needed.
     * @return this event for chain builder
     */
    public static AuditEventFilter create(AuditEventType... eventTypes) {
        return new AuditEventFilter(Arrays.asList(eventTypes));
    }

}
