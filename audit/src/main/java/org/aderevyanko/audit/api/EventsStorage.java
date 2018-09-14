package org.aderevyanko.audit.api;

import org.aderevyanko.audit.api.generic.GenericEventsStorage;

public interface EventsStorage extends GenericEventsStorage<AuditEventHeader, AuditEvent, AuditEventFilter> {
}
