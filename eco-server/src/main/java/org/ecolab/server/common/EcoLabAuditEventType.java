package org.ecolab.server.common;

import org.aderevyanko.audit.api.AuditEventType;

public enum EcoLabAuditEventType implements AuditEventType {
    LOGIN(0L);

    private final long id;

    EcoLabAuditEventType(long id) {
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
