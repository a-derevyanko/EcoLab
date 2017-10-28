package org.ekolab.client.vaadin.server.ui.development;

import org.springframework.boot.info.BuildProperties;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.vaadin.spring.security.shared.VaadinSharedSecurity;

/**
 * Утилиты для демо-версии.
 */
public class DevUtils {
    @Deprecated
    private static BuildProperties buildProperties;

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

    @Deprecated
    public static void setBuildProperties(BuildProperties buildProperties) {
        DevUtils.buildProperties = buildProperties;
    }

    @Deprecated
    public static boolean isProductionVersion() {
        return !buildProperties.getVersion().startsWith("0.") || System.getProperty("idea.paths.selector") != null;
    }
}
