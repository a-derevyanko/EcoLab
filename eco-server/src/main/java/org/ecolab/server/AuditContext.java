package org.ecolab.server;

import org.aderevyanko.audit.api.AuditService;
import org.aderevyanko.audit.api.AuditServiceImpl;
import org.aderevyanko.audit.api.EventsStorage;
import org.ecolab.server.common.EcoLabAuditEventAttribute;
import org.ecolab.server.common.EcoLabAuditEventType;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.concurrent.Executors;

/**
 * Created by Андрей on 06.09.2016.
 */
@SpringBootConfiguration
public class AuditContext {
    @Bean
    public AuditService auditService(EventsStorage storage) {
        return new AuditServiceImpl(
                storage,
                Executors.newFixedThreadPool(100),
                Arrays.asList(EcoLabAuditEventType.values()),
                Arrays.asList(EcoLabAuditEventAttribute.values()),
                10,
                60);
    }
}

