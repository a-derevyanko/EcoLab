package org.ecolab.server.service.impl;

import org.aderevyanko.audit.api.AuditEventFilter;
import org.aderevyanko.audit.api.generic.GenericEventsStorage;
import org.aderevyanko.audit.impl.GenericAuditServiceImpl;
import org.ecolab.server.common.EcoLabAuditEventAttribute;
import org.ecolab.server.common.EcoLabAuditEventType;
import org.ecolab.server.model.EcoLabAuditEvent;
import org.ecolab.server.model.EcoLabAuditEventHeader;
import org.ecolab.server.service.api.EcoLabAuditService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.Executors;

@Service
public class EcoLabAuditServiceImpl extends GenericAuditServiceImpl<EcoLabAuditEventHeader, EcoLabAuditEvent, AuditEventFilter>
        implements EcoLabAuditService {

    public EcoLabAuditServiceImpl(GenericEventsStorage<EcoLabAuditEventHeader, EcoLabAuditEvent, AuditEventFilter> storage) {
        super(storage,
                Executors.newFixedThreadPool(100),
                Arrays.asList(EcoLabAuditEventType.values()),
                Arrays.asList(EcoLabAuditEventAttribute.values()),
                10,
                60);
    }
}
