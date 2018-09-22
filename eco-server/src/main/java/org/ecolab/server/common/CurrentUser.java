package org.ecolab.server.common;

import org.ecolab.server.model.ClientContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

/**
 * Created by 777Al on 29.05.2017.
 */
public abstract class CurrentUser {
    private CurrentUser() {
        throw new AssertionError();
    }

    /**
     * Возвращает контекст текущей сессии
     * @return контекст текущей сессии
     */
    public static ClientContext getCurrentContext() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Objects.requireNonNull(securityContext, "Current security context is empty!");
        return (ClientContext) securityContext.getAuthentication().getDetails();
    }

    /**
     * Возвращает идентификатор текущего пользователя
     * @return идентификатор текущего пользователя
     */
    public static long getId() {
        return getCurrentContext().getUserId();
    }
}
