package org.aderevyanko.audit.api;

@FunctionalInterface
public interface AuditEventProducer {
    AuditEvent createEvent(AuditContext context);
}
