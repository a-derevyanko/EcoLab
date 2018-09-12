package org.aderevyanko.audit.impl;

import org.aderevyanko.audit.api.AuditEvent;
import org.aderevyanko.audit.api.AuditEventAttribute;
import org.aderevyanko.audit.api.AuditEventFilter;
import org.aderevyanko.audit.api.AuditEventHeader;
import org.aderevyanko.audit.api.AuditEventType;
import org.aderevyanko.audit.api.AuditService;
import org.aderevyanko.audit.api.base.BaseEventsStorage;

import java.util.List;
import java.util.concurrent.Executor;

public class AuditServiceImpl extends BaseAuditServiceImpl<AuditEventHeader, AuditEvent, AuditEventFilter>
        implements AuditService {

    public AuditServiceImpl(BaseEventsStorage<AuditEventHeader, AuditEvent, AuditEventFilter> storage,
                            Executor executor, List<AuditEventType> types, List<AuditEventAttribute> attributes,
                            int eventsSaveCount, int eventsSaveTimeSpanInSeconds) {
        super(storage, executor, types, attributes, eventsSaveCount, eventsSaveTimeSpanInSeconds);
    }
}
