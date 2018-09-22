package org.ecolab.server.common;

import org.aderevyanko.audit.api.AuditEventAttribute;

public enum EcoLabAuditEventAttribute implements AuditEventAttribute {
    USER_NAME;

    @Override
    public String getSystemName() {
        return name();
    }
}
