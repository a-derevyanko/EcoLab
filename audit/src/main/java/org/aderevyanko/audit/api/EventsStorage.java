package org.aderevyanko.audit.api;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface EventsStorage {
    void saveEvents(Collection<AuditEvent> events);

    Map<Long, String> getEventTypes();

    Map<Long, String> getEventAttributeTypes();

    Set<Long> getLoggableEvents();
}
