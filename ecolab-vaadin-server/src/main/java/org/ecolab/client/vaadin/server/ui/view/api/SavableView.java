package org.ecolab.client.vaadin.server.ui.view.api;

/**
 * Created by Андрей on 02.04.2017.
 */
public interface SavableView extends View {
    boolean saveData(boolean showErrors);
}
