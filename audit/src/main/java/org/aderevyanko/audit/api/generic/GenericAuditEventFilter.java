package org.aderevyanko.audit.api.generic;

import org.aderevyanko.audit.api.AuditEventType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public abstract class GenericAuditEventFilter<F extends GenericAuditEventFilter<F>> implements Serializable {
    protected final List<AuditEventType> eventTypes;

    protected final LocalDateTime startDate;

    protected final LocalDateTime endDate;

    protected GenericAuditEventFilter(LocalDateTime startDate, LocalDateTime endDate, List<AuditEventType> eventTypes) {
        Objects.requireNonNull(startDate);
        Objects.requireNonNull(endDate);
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventTypes = eventTypes;
    }

    public List<AuditEventType> getEventTypes() {
        return eventTypes;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
}
