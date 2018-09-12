package org.aderevyanko.audit.api;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class AuditEventHeader implements Serializable {
    private AuditEventType eventType;

    private Long id;

    private LocalDateTime  eventDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEventType(AuditEventType eventType) {
        this.eventType = eventType;
    }

    public AuditEventType getEventType() {
        return eventType;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuditEventHeader)) return false;
        AuditEventHeader that = (AuditEventHeader) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(eventType, that.eventType) &&
                Objects.equals(eventDate, that.eventDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventType, eventDate);
    }
}
