package org.aderevyanko.audit.api;

import org.aderevyanko.audit.api.generic.GenericAuditEventFilter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class AuditEventFilter extends GenericAuditEventFilter<AuditEventFilter> {
    private AuditEventFilter(LocalDateTime startDate, LocalDateTime endDate, List<AuditEventType> types) {
        super(startDate, endDate, types);
    }

    /**
     * Static constructor method
     * @param startDate start of event date range
     * @param endDate end of event date range
     * @param eventTypes types of events. Can be empty if all event types needed.
     * @return this event for chain builder
     */
    public static AuditEventFilter create(LocalDateTime startDate, LocalDateTime endDate, AuditEventType... eventTypes) {
        return new AuditEventFilter(startDate, endDate, Arrays.asList(eventTypes));
    }

}
