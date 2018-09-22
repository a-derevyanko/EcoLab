package org.ecolab.server.service.impl;

import org.aderevyanko.audit.api.generic.AuditConfigStorage;
import org.aderevyanko.audit.impl.EventAttributeProviderImpl;
import org.ecolab.server.common.EcoLabAuditEventAttribute;
import org.springframework.stereotype.Service;

@Service
public class EcoLabAuditEventAttributeProvider extends EventAttributeProviderImpl {
    protected EcoLabAuditEventAttributeProvider(AuditConfigStorage storage) {
        super(EcoLabAuditEventAttribute.class, storage);
    }
}
