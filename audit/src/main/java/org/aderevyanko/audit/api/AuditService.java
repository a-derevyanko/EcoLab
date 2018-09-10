package org.aderevyanko.audit.api;

/**
 * Cервис аудита
 */
public interface AuditService {
    /**
     * Запись события аудита в хранилище
     * @param event событие
     */
    void log(AuditEvent event);
}
