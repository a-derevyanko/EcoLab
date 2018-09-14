package org.ecolab.server.service.api;

import org.aderevyanko.audit.api.AuditEventFilter;
import org.aderevyanko.audit.api.generic.GenericAuditService;
import org.ecolab.server.model.EcoLabAuditEvent;
import org.ecolab.server.model.EcoLabAuditEventHeader;

public interface EcoLabAuditService extends GenericAuditService<EcoLabAuditEventHeader, EcoLabAuditEvent,  AuditEventFilter> {
}
