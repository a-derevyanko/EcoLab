package org.aderevyanko.audit.impl;

import org.aderevyanko.audit.api.AuditEvent;
import org.aderevyanko.audit.api.AuditEventFilter;
import org.aderevyanko.audit.api.AuditEventHeader;
import org.aderevyanko.audit.api.AuditService;
import org.aderevyanko.audit.api.generic.AuditConfigStorage;
import org.aderevyanko.audit.api.generic.GenericEventsStorage;

import java.util.concurrent.Executor;

public class AuditServiceImpl extends GenericAuditServiceImpl<AuditEventHeader, AuditEvent, AuditEventFilter>
        implements AuditService {

    protected AuditServiceImpl(GenericEventsStorage<AuditEventHeader, AuditEvent, AuditEventFilter> storage,
                               Executor executor,
                               int eventsSaveCount, int eventsSaveTimeSpanInSeconds, AuditConfigStorage configStorage) {
        super(storage, executor, eventsSaveCount, eventsSaveTimeSpanInSeconds, configStorage);
    }
}
