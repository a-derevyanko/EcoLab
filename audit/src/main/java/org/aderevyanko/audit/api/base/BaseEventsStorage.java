package org.aderevyanko.audit.api.base;

import org.aderevyanko.audit.api.AuditEventHeader;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface BaseEventsStorage<H extends AuditEventHeader, T extends BaseAuditEvent<H, T>, F extends BaseAuditEventFilter<H>> {
    void saveEvents(Collection<T> events);

    Map<Long, String> getEventTypes();

    Map<Long, String> getEventAttributeTypes();

    Set<Long> getLoggableEvents();

    Set<F> getHeaders();
}
