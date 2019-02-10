package org.aderevyanko.audit.impl;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.aderevyanko.audit.api.AuditEventContext;
import org.aderevyanko.audit.api.AuditEventHeader;
import org.aderevyanko.audit.api.generic.AuditConfigStorage;
import org.aderevyanko.audit.api.generic.GenericAuditEvent;
import org.aderevyanko.audit.api.generic.GenericAuditEventFilter;
import org.aderevyanko.audit.api.generic.GenericAuditService;
import org.aderevyanko.audit.api.generic.GenericEventsStorage;
import reactor.core.Disposable;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public abstract class GenericAuditServiceImpl<H extends AuditEventHeader, T extends GenericAuditEvent<T>, F extends GenericAuditEventFilter<F>>
        implements GenericAuditService<H, T, F> {
    private static final Logger LOGGER = Logger.getLogger(GenericAuditServiceImpl.class.getName());

    protected final EmitterProcessor<T> messageObserver = EmitterProcessor.create();

    private final int eventsSaveCount;

    private final int eventsSaveTimeSpanInSeconds;

    private final AuditConfigStorage configStorage;

    private final GenericEventsStorage<H, T, F> storage;

    public GenericAuditServiceImpl(GenericEventsStorage<H, T, F> storage,
                                   Executor executor, int eventsSaveCount, int eventsSaveTimeSpanInSeconds,
                                   AuditConfigStorage configStorage) {
        this.storage = storage;
        this.eventsSaveCount = eventsSaveCount;
        this.eventsSaveTimeSpanInSeconds = eventsSaveTimeSpanInSeconds;
        this.configStorage = configStorage;
        Scheduler scheduler = Schedulers.fromExecutor(executor);

        // Subscribe for events
        Disposable subscription = messageObserver.
                filter(message -> {
                    if (message.getEventDate() == null) {
                        LOGGER.severe("Error! Empty event date: " + message);
                    } else if (message.getEventType() == null) {
                        LOGGER.severe("Error! Empty event type: " + message);
                    } else if (message.getId() != null) {
                        LOGGER.severe("Error! Event id not empty: " + message);
                    } else {
                        return this.configStorage.getLoggableEvents().contains(message.getEventType().getSystemName());
                    }
                    return false;
                }).map(this::createAuditEventContext).
                publishOn(scheduler).
                bufferTimeout(this.eventsSaveCount, Duration.ofSeconds(this.eventsSaveTimeSpanInSeconds)).
                filter(messages -> !messages.isEmpty()).
                subscribe(storage::saveEvents);
        Runtime.getRuntime().addShutdownHook(new Thread(subscription::dispose));
    }

    @Override
    public void log(T event) {
        messageObserver.onNext(event);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("message write: " + event);
        }
    }

    @Override
    public Set<H> getHeaders(F filter) {
        return storage.getHeaders(filter);
    }

    protected abstract AuditEventContext<T> createAuditEventContext(T event);
}
