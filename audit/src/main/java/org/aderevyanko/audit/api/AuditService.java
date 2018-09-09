package org.aderevyanko.audit.api;

public interface AuditService {
    void log(AuditEvent event);
}
