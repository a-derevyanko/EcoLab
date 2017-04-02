package org.ekolab.client.vaadin.server.ui;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.view.BaseView;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * При изменении VIEW меняются кнопки в тулбаре.
 */
@UIScope
@SpringComponent
public class EkoLabMenuBar extends MenuBar implements ViewChangeListener {
    @Autowired
    private I18N i18N;

    @Autowired
    private EkoLabNavigator navigator;

    // ---------------------- Данные экземпляра ----------------------------------

    // ---------------------- Графические компоненты -----------------------------
    private final HorizontalLayout leftButtonPanel = new HorizontalLayout();
    private final HorizontalLayout rightButtonPanel = new HorizontalLayout();

    @PostConstruct
    protected void init() {
        //setWidth(100.0F, Unit.PERCENTAGE);
    }

    @Override
    public MenuItem addItem(String caption, Resource icon, Command command) {
        return super.addItem(i18N.get(caption), icon, command);
    }

    @Override
    public MenuItem addItemBefore(String caption, Resource icon, Command command, MenuItem itemToAddBefore) {
        return super.addItemBefore(i18N.get(caption), icon, command, itemToAddBefore);
    }

    // ------------------------------ Реализация обработчиков событий ----------------
    @Override
    public boolean beforeViewChange(ViewChangeEvent event) {
        return true;
    }

    @Override
    public void afterViewChange(ViewChangeEvent event) {
        ((BaseView) event.getNewView()).placeMenuBarActions(this);
    }
}
