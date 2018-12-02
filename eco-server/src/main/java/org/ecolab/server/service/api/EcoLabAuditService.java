package org.ecolab.server.service.api;

import java.util.function.Consumer;
import org.aderevyanko.audit.api.AuditEventFilter;
import org.aderevyanko.audit.api.generic.GenericAuditService;
import org.ecolab.server.model.EcoLabAuditEvent;
import org.ecolab.server.model.EcoLabAuditEventHeader;

public interface EcoLabAuditService extends GenericAuditService<EcoLabAuditEventHeader, EcoLabAuditEvent,  AuditEventFilter> {
    /**
     * Добавление подписчика событий
     * @param subscriber подписчик
     */
    void addEventSubscriber(Consumer<EcoLabAuditEvent> subscriber);
}
