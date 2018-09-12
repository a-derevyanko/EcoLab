package org.aderevyanko.audit.api;

import org.aderevyanko.audit.api.base.BaseEventsStorage;

public interface EventsStorage extends BaseEventsStorage<AuditEventHeader, AuditEvent, AuditEventFilter> {
}
