package org.ekolab.client.vaadin.server.ui.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.MenuBar;
import org.ekolab.client.vaadin.server.ui.EkoLabMenuBar;

/**
 * Created by Андрей on 02.04.2017.
 */
public interface SavableView extends BaseView {
    @Override
    default void placeMenuBarActions(EkoLabMenuBar toolBar) {
        toolBar.addItem("savable.save", VaadinIcons.DISC, (MenuBar.Command) selectedItem -> saveData());
    }

    void saveData();
}
