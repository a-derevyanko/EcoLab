package org.aderevyanko.audit.api.generic;

import org.aderevyanko.audit.api.AuditEventAttribute;
import org.aderevyanko.audit.api.AuditEventType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GenericAuditEventFilter<F extends GenericAuditEventFilter<F>> implements Serializable {
    protected final List<AuditEventType> eventTypes;

    protected LocalDateTime startDate;

    protected LocalDateTime endDate;

    protected final Map<AuditEventAttribute, String> attributes = new HashMap<>();

    protected GenericAuditEventFilter(List<AuditEventType> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public GenericAuditEventFilter setAttribute(AuditEventAttribute attribute, String value) {
        if (value == null) {
            attributes.remove(attribute);
        } else {
            attributes.putIfAbsent(attribute, value);
        }
        return this;
    }

    public F startDate(LocalDateTime startDate) {
        this.startDate = startDate;
        return (F) this;
    }


    public F endDate(LocalDateTime endDate) {
        this.endDate = endDate;
        return (F) this;
    }

    public Map<AuditEventAttribute, String> getAttributes() {
        return attributes;
    }
}
