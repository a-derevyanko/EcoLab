package org.ekolab.client.vaadin.server.service;

import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by Андрей on 27.11.2016.
 */
@Service
@UIScope
public class I18N implements Serializable {
    private static final String CACHE_NAME = "I18N";

    @Autowired
    private final MessageSource messageSource;

    @Autowired
    private final VaadinSession vaadinSession;

    public I18N(MessageSource messageSource, VaadinSession vaadinSession) {
        this.messageSource = messageSource;
        this.vaadinSession = vaadinSession;
    }

    @Cacheable(CACHE_NAME)
    public String get(String key, Object... args) {
        return messageSource.getMessage(key, args, vaadinSession.getLocale());
    }
}
