package org.ekolab.client.vaadin.server.ui.view.api;

import com.vaadin.navigator.ViewChangeListener;
import org.ekolab.client.vaadin.server.ui.EkoLabMenuBar;

/**
 * Created by Андрей on 02.04.2017.
 */
public interface View extends UIComponent, com.vaadin.navigator.View {
    @Deprecated
    default void placeMenuBarActions(EkoLabMenuBar toolBar) {}

    @Override
    default void enter(ViewChangeListener.ViewChangeEvent event) {}
}
