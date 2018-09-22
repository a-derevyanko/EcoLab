package org.aderevyanko.audit.api.generic;

import java.util.Map;
import java.util.Set;

public interface AuditConfigStorage {
    Map<Long, String> getEventTypes();

    Map<Long, String> getEventAttributeTypes();

    Set<String> getLoggableEvents();
}
