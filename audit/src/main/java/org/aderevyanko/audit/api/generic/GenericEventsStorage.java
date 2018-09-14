package org.aderevyanko.audit.api.generic;

import org.aderevyanko.audit.api.AuditEventHeader;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface GenericEventsStorage<H extends AuditEventHeader, T extends GenericAuditEvent<T>, F extends GenericAuditEventFilter<F>> {
    void saveEvents(Collection<T> events);

    Map<Long, String> getEventTypes();

    Map<Long, String> getEventAttributeTypes();

    Set<Long> getLoggableEvents();

    Set<H> getHeaders(F filter);

    Set<T> getEvents(F filter);

    T getEvent(long id);
}
