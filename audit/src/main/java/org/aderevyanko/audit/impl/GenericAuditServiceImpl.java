package org.aderevyanko.audit.impl;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import org.aderevyanko.audit.api.AuditEventHeader;
import org.aderevyanko.audit.api.generic.AuditConfigStorage;
import org.aderevyanko.audit.api.generic.GenericAuditEvent;
import org.aderevyanko.audit.api.generic.GenericAuditEventFilter;
import org.aderevyanko.audit.api.generic.GenericAuditService;
import org.aderevyanko.audit.api.generic.GenericEventsStorage;

import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class GenericAuditServiceImpl<H extends AuditEventHeader, T extends GenericAuditEvent<T>, F extends GenericAuditEventFilter<F>>
        implements GenericAuditService<H, T, F> {
    private static final Logger LOGGER = Logger.getLogger(GenericAuditServiceImpl.class.getName());

    private final PublishSubject<T> messageObserver = PublishSubject.create();

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
        Scheduler scheduler = Schedulers.from(executor);

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
                }).
                observeOn(scheduler).
                buffer(this.eventsSaveTimeSpanInSeconds, TimeUnit.SECONDS, this.eventsSaveCount).
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
}
