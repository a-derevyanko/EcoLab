package org.aderevyanko.audit.api.generic;

import org.aderevyanko.audit.api.AuditEventHeader;

import java.util.Set;

/**
 * Cервис аудита
 */
public interface GenericAuditService<H extends AuditEventHeader, T extends GenericAuditEvent<T>, F extends GenericAuditEventFilter<F>> {
    /**
     * Запись события аудита в хранилище
     * @param event событие
     */
    void log(T event);

    Set<H> getHeaders(F filter);
}
