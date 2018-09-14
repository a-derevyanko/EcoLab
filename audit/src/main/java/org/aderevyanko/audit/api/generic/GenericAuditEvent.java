package org.aderevyanko.audit.api.generic;

import org.aderevyanko.audit.api.AuditEventAttribute;
import org.aderevyanko.audit.api.AuditEventType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract audit event model.
 * If you use your own audit event header, this class should be inherited to add new chain builder methods.
 */
public abstract class GenericAuditEvent<T extends GenericAuditEvent<T>> implements Serializable {
    private final AuditEventType eventType;

    private Long id;

    private LocalDateTime  eventDate;

    protected final Map<AuditEventAttribute, String> attributes = new HashMap<>();

    protected GenericAuditEvent(AuditEventType eventType) {
        this.eventType = eventType;
    }

    public T attribute(AuditEventAttribute attribute, String value) {
        if (value == null) {
            attributes.remove(attribute);
        } else {
            attributes.putIfAbsent(attribute, value);
        }
        return (T) this;
    }

    public T id(Long id) {
        this.id = id;
        return (T) this;
    }

    public T eventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
        return (T) this;
    }

    public Map<AuditEventAttribute, String> getAttributes() {
        return attributes;
    }

    public AuditEventType getEventType() {
        return eventType;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }
}
