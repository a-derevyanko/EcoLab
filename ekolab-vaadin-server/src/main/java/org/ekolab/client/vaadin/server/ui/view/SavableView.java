package org.ekolab.client.vaadin.server.ui.view;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.AlignmentInfo;
import com.vaadin.ui.Button;
import org.ekolab.client.vaadin.server.ui.EkoLabToolBar;

/**
 * Created by Андрей on 02.04.2017.
 */
public interface SavableView extends BaseView {
    @Override
    default void placeToolBarActions(EkoLabToolBar toolBar) {
        Button saveButton = new Button(VaadinIcons.DISC);
        toolBar.addButton(saveButton, "savable.save", AlignmentInfo.LEFT);
    }
}
