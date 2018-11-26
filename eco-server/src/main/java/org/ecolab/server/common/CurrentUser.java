package org.ecolab.server.common;

import java.util.Locale;
import org.ecolab.server.model.ClientContextProvider;

/**
 * Created by 777Al on 29.05.2017.
 */
public abstract class CurrentUser {
    private CurrentUser() {
        throw new AssertionError();
    }

    /**
     * Контекст текущей сессии
     */
    private static ClientContextProvider provider;

    public static void setProvider(ClientContextProvider provider) {
        CurrentUser.provider = provider;
    }

    /**
     * Возвращает идентификатор текущего пользователя
     * @return идентификатор текущего пользователя
     */
    public static Long getId() {
        return provider.getClientContext().getUserId();
    }

    /**
     * Возвращает язык текущего пользователя
     * @return язык текущего пользователя
     */
    public static Locale getLocale() {
        return provider.getClientContext().getLocale();
    }
}
