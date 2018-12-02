package org.aderevyanko.audit.api.generic;

import org.aderevyanko.audit.api.AuditEventContext;
import org.aderevyanko.audit.api.AuditEventHeader;

import java.util.Collection;
import java.util.Set;

public interface GenericEventsStorage<H extends AuditEventHeader,
        T extends GenericAuditEvent<T>, F extends GenericAuditEventFilter<F>> {
    void saveEvents(Collection<AuditEventContext<T>> events);

    Set<H> getHeaders(F filter);

    T getEvent(long id);
}
