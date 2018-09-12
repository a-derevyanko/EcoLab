package org.aderevyanko.audit.impl;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import org.aderevyanko.audit.api.AuditEventAttribute;
import org.aderevyanko.audit.api.AuditEventHeader;
import org.aderevyanko.audit.api.AuditEventType;
import org.aderevyanko.audit.api.base.BaseAuditService;
import org.aderevyanko.audit.api.base.BaseAuditEvent;
import org.aderevyanko.audit.api.base.BaseAuditEventFilter;
import org.aderevyanko.audit.api.base.BaseEventsStorage;
import org.aderevyanko.audit.api.StoredType;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class BaseAuditServiceImpl<H extends AuditEventHeader, T extends BaseAuditEvent<H, T>, F extends BaseAuditEventFilter<H>>
        implements BaseAuditService<H, T, F> {
    private static final Logger LOGGER = Logger.getLogger(BaseAuditServiceImpl.class.getName());

    private final PublishSubject<T> messageObserver = PublishSubject.create();

    private final List<AuditEventType> types;

    private final List<AuditEventAttribute> attributes;

    private final int eventsSaveCount;

    private final int eventsSaveTimeSpanInSeconds;

    private final BaseEventsStorage<H, T, F> storage;

    public BaseAuditServiceImpl(BaseEventsStorage<H, T, F> storage, Executor executor, List<AuditEventType> types,
                                List<AuditEventAttribute> attributes, int eventsSaveCount, int eventsSaveTimeSpanInSeconds) {
        this.storage = storage;
        this.types = types;
        this.attributes = attributes;
        this.eventsSaveCount = eventsSaveCount;
        this.eventsSaveTimeSpanInSeconds = eventsSaveTimeSpanInSeconds;
        checkEnumIdUnique(this.types);
        checkEnumIdUnique(this.attributes);
        Scheduler scheduler = Schedulers.from(executor);

        Map<Long, String> storedEventTypes = storage.getEventTypes();

        // Check, that all application events and attributes are registered in storage
        for (AuditEventType type : this.types) {
            if (storedEventTypes.containsKey(type.getId())) {
                if (!storedEventTypes.get(type.getId()).equals(type.getSystemName())) {
                    throw new IllegalArgumentException("Event type '" + type.getSystemName() + "' system name is differ from storage!");
                }
            } else {
                throw new IllegalArgumentException("Event type '" + type.getSystemName() + "' not registered in storage!");
            }
        }

        Map<Long, String> storedEventAttributeTypes = storage.getEventAttributeTypes();

        for (AuditEventAttribute type : this.attributes) {
            if (storedEventAttributeTypes.containsKey(type.getId())) {
                if (!storedEventAttributeTypes.get(type.getId()).equals(type.getSystemName())) {
                    throw new IllegalArgumentException("Attribute type '" + type.getSystemName() + "' system name is differ from storage!");
                }
            } else {
                throw new IllegalArgumentException("Attribute type '" + type.getSystemName() + "' not registered in storage!");
            }
        }

        // Subscribe for events
        Disposable subscription = messageObserver.
                filter(message -> {
                    if (message.getHeader().getEventDate() == null) {
                        LOGGER.severe("Error! Empty event date: " + message);
                    } else if (message.getHeader().getEventType() == null) {
                        LOGGER.severe("Error! Empty event type: " + message);
                    } else if (message.getHeader().getId() != null) {
                        LOGGER.severe("Error! Event id not empty: " + message);
                    } else {
                        return storage.getLoggableEvents().contains(message.getHeader().getEventType().getId());
                    }
                    return false;
                }).
                observeOn(scheduler).
                buffer(this.eventsSaveTimeSpanInSeconds, TimeUnit.SECONDS, this.eventsSaveCount).
                filter(messages -> !messages.isEmpty()).
                subscribe(storage::saveEvents);
    }

    private static void checkEnumIdUnique(Collection<? extends StoredType> values) {
        values.stream().collect(Collectors.groupingBy(
                Function.identity(), Collectors.counting()))
                .forEach((key, value) -> {
                    if (value > 1) {
                        throw new IllegalArgumentException("Duplicate type: " +
                                key.getClass() + ": " + key.getSystemName());
                    }
                });
    }


    @Override
    public void log(T event) {
        messageObserver.onNext(event);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("message write: " + event);
        }
    }

    @Override
    public Set<F> getHeaders() {
        return storage.getHeaders();
    }
}
