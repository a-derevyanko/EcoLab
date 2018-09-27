package org.aderevyanko.audit.api;

import org.aderevyanko.audit.api.generic.GenericAuditEvent;

import java.util.HashMap;
import java.util.Map;

public class AuditEventContext<T extends GenericAuditEvent<T>> {
    private final T auditEvent;
    private final Map<Enum<?>, Object> contextValues = new HashMap<>();

    public AuditEventContext(T auditEvent) {
        this.auditEvent = auditEvent;
    }

    public void setValue(Enum<?> name, Object value) {
        contextValues.put(name, value);
    }

    public <V> V getValue(Enum<?> name) {
        return (V) contextValues.get(name);
    }

    public T getAuditEvent() {
        return auditEvent;
    }
}
