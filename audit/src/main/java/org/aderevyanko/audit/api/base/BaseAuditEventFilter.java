package org.aderevyanko.audit.api.base;

import org.aderevyanko.audit.api.AuditEventAttribute;
import org.aderevyanko.audit.api.AuditEventHeader;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseAuditEventFilter<H extends AuditEventHeader> implements Serializable {
    protected final H header;

    protected final Map<AuditEventAttribute, String> attributes = new HashMap<>();

    protected BaseAuditEventFilter(H header) {
        this.header = header;
    }

    public BaseAuditEventFilter<H> setAttribute(AuditEventAttribute attribute, String value) {
        if (value == null) {
            attributes.remove(attribute);
        } else {
            attributes.putIfAbsent(attribute, value);
        }
        return this;
    }

    public BaseAuditEventFilter<H> eventDate(LocalDateTime eventDate) {
        this.header.setEventDate(eventDate);
        return this;
    }

    public Map<AuditEventAttribute, String> getAttributes() {
        return attributes;
    }
}
