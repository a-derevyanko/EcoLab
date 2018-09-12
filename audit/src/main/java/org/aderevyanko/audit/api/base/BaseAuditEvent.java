package org.aderevyanko.audit.api.base;

import org.aderevyanko.audit.api.AuditEventAttribute;
import org.aderevyanko.audit.api.AuditEventHeader;
import org.aderevyanko.audit.api.AuditEventType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Abstract audit event model.
 * If you use your own audit event header, this class should be inherited to add new chain builder methods.
 * @param <H> type of audit event header
 */
public abstract class BaseAuditEvent<H extends AuditEventHeader, T extends BaseAuditEvent<H, T>> implements Serializable {
    protected final H header;

    protected final Map<AuditEventAttribute, String> attributes = new HashMap<>();

    protected BaseAuditEvent(H header, AuditEventType eventType) {
        Objects.requireNonNull(header);
        Objects.requireNonNull(eventType);
        this.header = header;
        this.header.setEventType(eventType);
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
        this.header.setId(id);
        return (T) this;
    }

    public T eventDate(LocalDateTime eventDate) {
        this.header.setEventDate(eventDate);
        return (T) this;
    }

    public Map<AuditEventAttribute, String> getAttributes() {
        return attributes;
    }

    public H getHeader() {
        return header;
    }
}
