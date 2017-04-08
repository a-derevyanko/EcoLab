package org.ekolab.client.vaadin.server.ui.view.api;

import com.vaadin.icons.VaadinIcons;
import org.ekolab.client.vaadin.server.ui.EkoLabMenuBar;

/**
 * Created by Андрей on 02.04.2017.
 */
public interface SavableView extends View {
    @Override
    default void placeMenuBarActions(EkoLabMenuBar toolBar) {
        toolBar.addItem("savable.save", VaadinIcons.DISC, selectedItem -> saveData());
    }

    void saveData();
}
