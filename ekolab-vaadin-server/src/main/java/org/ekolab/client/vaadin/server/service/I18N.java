package org.ekolab.client.vaadin.server.service;

import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.UIScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by Андрей on 27.11.2016.
 */
@Service
@UIScope
public class I18N implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(I18N.class);

    @Autowired
    private final MessageSource messageSource;

    @Autowired
    private final VaadinSession vaadinSession;

    public I18N(MessageSource messageSource, VaadinSession vaadinSession) {
        this.messageSource = messageSource;
        this.vaadinSession = vaadinSession;
    }

    @Cacheable("I18N")
    public String get(String key, Object... args) {
        try {
            return messageSource.getMessage(key, args, vaadinSession.getLocale());
        } catch (NoSuchMessageException ex) {
            LOGGER.error(ex.getLocalizedMessage());
            return "";
        }
    }
}
