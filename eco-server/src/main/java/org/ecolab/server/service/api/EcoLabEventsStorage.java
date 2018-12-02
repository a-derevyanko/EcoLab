package org.ecolab.server.service.api;

import org.aderevyanko.audit.api.AuditEventFilter;
import org.aderevyanko.audit.api.generic.GenericEventsStorage;
import org.ecolab.server.model.EcoLabAuditEvent;
import org.ecolab.server.model.EcoLabAuditEventHeader;

public interface EcoLabEventsStorage extends GenericEventsStorage<EcoLabAuditEventHeader, EcoLabAuditEvent, AuditEventFilter> {
    int getHeadersSize(AuditEventFilter filter);
}
