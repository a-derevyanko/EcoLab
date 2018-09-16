package org.ecolab.server.common;

import org.aderevyanko.audit.api.AuditEventType;

public enum EcoLabAuditEventType implements AuditEventType {
    LOGIN,
    LOGOUT;

    @Override
    public String getSystemName() {
        return name();
    }
}
