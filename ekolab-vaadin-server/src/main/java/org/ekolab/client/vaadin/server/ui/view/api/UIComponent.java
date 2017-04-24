package org.ekolab.client.vaadin.server.ui.view.api;

import com.vaadin.server.Sizeable;

import javax.annotation.PostConstruct;

/**
 * Created by Андрей on 02.04.2017.
 */
public interface UIComponent extends Sizeable {
    @PostConstruct
    default void init() throws Exception { setSizeFull();}
}
