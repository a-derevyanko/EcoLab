package org.aderevyanko.audit.api;

import org.aderevyanko.audit.api.generic.GenericAuditService;

/**
 * Cервис аудита
 */
public interface AuditService extends GenericAuditService<AuditEventHeader, AuditEvent,  AuditEventFilter> {
}
