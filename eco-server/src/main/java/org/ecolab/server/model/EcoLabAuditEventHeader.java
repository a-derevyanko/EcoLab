package org.ecolab.server.model;

import org.aderevyanko.audit.api.AuditEventHeader;

public class EcoLabAuditEventHeader extends AuditEventHeader {
    /**
     * Логин инициатора события
     */
    private String initiator;

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }
}