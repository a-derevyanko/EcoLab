package org.ecolab.server.service.impl;

import org.aderevyanko.audit.api.generic.AuditConfigStorage;
import org.aderevyanko.audit.impl.EventTypeProviderImpl;
import org.ecolab.server.common.EcoLabAuditEventType;
import org.springframework.stereotype.Service;

@Service
public class EcoLabAuditEventTypeProvider extends EventTypeProviderImpl {
    protected EcoLabAuditEventTypeProvider(AuditConfigStorage storage) {
        super(EcoLabAuditEventType.class, storage);
    }
}
