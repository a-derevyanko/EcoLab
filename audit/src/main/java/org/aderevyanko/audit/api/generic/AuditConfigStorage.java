package org.aderevyanko.audit.api.generic;

import java.util.Map;
import java.util.Set;

public interface AuditConfigStorage {
    Map<String, String> getEventNames();

    Map<Long, String> getEventTypes();

    Map<String, String> getEventAttributeNames();

    Map<Long, String> getEventAttributeTypes();

    Set<String> getLoggableEvents();
}
