package org.ecolab.client.vaadin.server.ui.view.api;

import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener;

/**
 * Created by Андрей on 02.04.2017.
 */
public interface View extends UIComponent, com.vaadin.navigator.View {

    @Override
    default void enter(ViewChangeListener.ViewChangeEvent event) {
        com.vaadin.navigator.View.super.enter(event);
    }

    @Override
    default void beforeLeave(ViewBeforeLeaveEvent event) {
        com.vaadin.navigator.View.super.beforeLeave(event);
    }
}
