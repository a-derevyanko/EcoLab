package org.aderevyanko.audit.api;

import org.aderevyanko.audit.api.base.BaseAuditService;

/**
 * Cервис аудита
 */
public interface AuditService extends BaseAuditService<AuditEventHeader, AuditEvent,  AuditEventFilter> {
}
