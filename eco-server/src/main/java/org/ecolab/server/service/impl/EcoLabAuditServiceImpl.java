package org.ecolab.server.service.impl;

import java.util.function.Consumer;
import java.util.logging.Logger;
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
import reactor.core.Disposable;

public class EcoLabAuditServiceImpl extends GenericAuditServiceImpl<EcoLabAuditEventHeader, EcoLabAuditEvent, AuditEventFilter>
        implements EcoLabAuditService {
    private static final Logger LOGGER = Logger.getLogger(EcoLabAuditServiceImpl.class.getName());

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

    @Override
    public void addEventSubscriber(Consumer<EcoLabAuditEvent> subscriber) {
        Disposable subscription = messageObserver.subscribe(subscriber);
        Runtime.getRuntime().addShutdownHook(new Thread(subscription::dispose));
    }
}
