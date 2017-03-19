package org.ekolab.client.vaadin.server.service;

import com.vaadin.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

/**
 * Created by Андрей on 27.11.2016.
 */
@Service
public class I18N {
    @Autowired
    private final MessageSource messageSource;

    public I18N(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String get(String key, Object... args) {
        return messageSource.getMessage(key, args, VaadinSession.getCurrent().getLocale());
    }
}
