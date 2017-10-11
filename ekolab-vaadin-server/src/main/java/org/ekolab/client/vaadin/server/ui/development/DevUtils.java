package org.ekolab.client.vaadin.server.ui.development;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.vaadin.spring.security.shared.VaadinSharedSecurity;

/**
 * Утилиты для демо-версии.
 */
public class DevUtils {
    /**
     * Если есть анонимная сессия, производится попытка входа в систему
     * @param vaadinSecurity сервис аутентификации
     */
    public static void authenticateAsAdmin(VaadinSharedSecurity vaadinSecurity) {
        if (vaadinSecurity.isAuthenticatedAnonymously()) {
            try {
                vaadinSecurity.login("admin", "admin", false);
            } catch (Exception e) {
                throw new InternalAuthenticationServiceException(e.getMessage(), e);
            }
        }
    }
}
