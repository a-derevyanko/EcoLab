package org.ekolab.client.vaadin.server.ui.view;

import com.vaadin.navigator.View;
import org.ekolab.client.vaadin.server.ui.EkoLabMenuBar;

/**
 * Created by Андрей on 02.04.2017.
 */
public interface BaseView extends View {
    default void placeMenuBarActions(EkoLabMenuBar toolBar) {};
}
