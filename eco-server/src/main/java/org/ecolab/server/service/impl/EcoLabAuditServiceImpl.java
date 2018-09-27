package org.ecolab.server.service.impl;

import org.aderevyanko.audit.api.AuditEventContext;
import org.aderevyanko.audit.api.AuditEventFilter;
import org.aderevyanko.audit.api.generic.AuditConfigStorage;
import org.aderevyanko.audit.api.generic.GenericEventsStorage;
import org.aderevyanko.audit.impl.GenericAuditServiceImpl;
import org.ecolab.server.common.CurrentUser;
import org.ecolab.server.model.EcoLabAuditContextAttributes;
import org.ecolab.server.model.EcoLabAuditEvent;
import org.ecolab.server.model.EcoLabAuditEventHeader;
import org.ecolab.server.service.api.EcoLabAuditService;
import org.springframework.stereotype.Service;

@Service
public class EcoLabAuditServiceImpl extends GenericAuditServiceImpl<EcoLabAuditEventHeader, EcoLabAuditEvent, AuditEventFilter>
        implements EcoLabAuditService {

    public EcoLabAuditServiceImpl(GenericEventsStorage<EcoLabAuditEventHeader, EcoLabAuditEvent, AuditEventFilter> storage,
                                  AuditConfigStorage configStorage) {
        super(storage,
                SystemTaskExecutor.INSTANCE,
                10,
                60,
                configStorage);
    }

    @Override
    protected AuditEventContext<EcoLabAuditEvent> createAuditEventContext(EcoLabAuditEvent event) {
        AuditEventContext<EcoLabAuditEvent> context = new AuditEventContext<>(event);
        context.setValue(EcoLabAuditContextAttributes.USER_ID, CurrentUser.getId());
        return context;
    }
}
