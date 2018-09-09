package org.ecolab.server.common;

import org.aderevyanko.audit.api.AuditEventAttribute;

public enum EcoLabAuditEventAttribute implements AuditEventAttribute {
    USER_NAME(0L);

    private final long id;

    EcoLabAuditEventAttribute(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getSystemName() {
        return name();
    }
}
