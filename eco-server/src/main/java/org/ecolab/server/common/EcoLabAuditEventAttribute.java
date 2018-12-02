package org.ecolab.server.common;

import org.aderevyanko.audit.api.AuditEventAttribute;

public enum EcoLabAuditEventAttribute implements AuditEventAttribute {
    LAB_NUMBER,
    CONSUMER_NAME,
    USER_NAME;

    @Override
    public String getSystemName() {
        return name();
    }
}
