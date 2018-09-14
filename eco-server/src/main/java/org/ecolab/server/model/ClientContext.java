package org.ecolab.server.model;

import java.io.Serializable;
import java.util.Locale;

public class ClientContext implements Serializable {
    private final Long userId;
    private final Locale locale;

    public ClientContext(Long userId, Locale locale) {
        this.userId = userId;
        this.locale = locale;
    }

    public Long getUserId() {
        return userId;
    }

    public Locale getLocale() {
        return locale;
    }
}
