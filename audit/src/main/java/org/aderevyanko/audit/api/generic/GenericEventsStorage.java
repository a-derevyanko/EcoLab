package org.aderevyanko.audit.api.generic;

import org.aderevyanko.audit.api.AuditEventHeader;

import java.util.Collection;
import java.util.Set;

public interface GenericEventsStorage<H extends AuditEventHeader, T extends GenericAuditEvent<T>, F extends GenericAuditEventFilter<F>> {
    void saveEvents(Collection<T> events);

    Set<H> getHeaders(F filter);

    Set<T> getEvents(F filter);

    T getEvent(long id);
}
