package org.ecolab.server.common;

import org.aderevyanko.audit.api.AuditEventType;

public enum EcoLabAuditEventType implements AuditEventType {
    LOGIN,
    LOGOUT,
    LAB_DISALLOWED,
    LAB_ALLOWED,
    LAB_DEFENCE_DISALLOWED,
    LAB_DEFENCE_ALLOWED;

    @Override
    public String getSystemName() {
        return name();
    }
}
