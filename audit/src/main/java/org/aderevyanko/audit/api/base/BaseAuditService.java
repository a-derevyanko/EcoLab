package org.aderevyanko.audit.api.base;

import org.aderevyanko.audit.api.AuditEventHeader;

import java.util.Set;

/**
 * Cервис аудита
 */
public interface BaseAuditService<H extends AuditEventHeader, T extends BaseAuditEvent<H, T>, F extends BaseAuditEventFilter<H>> {
    /**
     * Запись события аудита в хранилище
     * @param event событие
     */
    void log(T event);

    Set<F> getHeaders();
}
