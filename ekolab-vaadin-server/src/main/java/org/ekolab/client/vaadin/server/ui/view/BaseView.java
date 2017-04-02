package org.ekolab.client.vaadin.server.ui.view;

import com.vaadin.navigator.View;
import org.ekolab.client.vaadin.server.ui.EkoLabToolBar;

/**
 * Created by Андрей on 02.04.2017.
 */
public interface BaseView extends View {
    default void placeToolBarActions(EkoLabToolBar toolBar) {};
}
