package org.aderevyanko.audit.api;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class AuditEvent implements Serializable {
    private Long id;

    private AuditEventType eventType;

    private LocalDateTime eventDate = LocalDateTime.now();

    private final Map<AuditEventAttribute, String> attributes = new HashMap<>();

    /**
     * Static constructor method
     * @param eventType type of an event
     * @return this event for chain builder
     */
    public static AuditEvent forType(AuditEventType eventType) {
        AuditEvent event = new AuditEvent();
        event.eventType = eventType;
        return event;
    }

    public AuditEvent setAttribute(AuditEventAttribute attribute, String value) {
        if (value == null) {
            attributes.remove(attribute);
        } else {
            attributes.putIfAbsent(attribute, value);
        }
        return this;
    }

    public Long getId() {
        return id;
    }

    public AuditEvent setId(Long id) {
        this.id = id;
        return this;
    }

    public AuditEventType getEventType() {
        return eventType;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public AuditEvent setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
        return this;
    }

    public Map<AuditEventAttribute, String> getAttributes() {
        return attributes;
    }
}
