package org.ecolab.server.service.impl;

import org.aderevyanko.audit.api.AuditServiceImpl;
import org.aderevyanko.audit.api.EventsStorage;
import org.ecolab.server.common.EcoLabAuditEventAttribute;
import org.ecolab.server.common.EcoLabAuditEventType;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.Executors;

@Service
public class EcoLabAuditServiceImpl extends AuditServiceImpl {
    public EcoLabAuditServiceImpl(EventsStorage storage) {
        super(storage,
                Executors.newFixedThreadPool(100),
                Arrays.asList(EcoLabAuditEventType.values()),
                Arrays.asList(EcoLabAuditEventAttribute.values()),
                10,
                60);
    }
}
