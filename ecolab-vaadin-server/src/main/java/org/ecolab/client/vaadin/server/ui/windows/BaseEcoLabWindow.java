package org.ecolab.client.vaadin.server.ui.windows;

import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import javax.annotation.PostConstruct;

/**
 * Created by 777Al on 03.04.2017.
 */
public abstract class BaseEcoLabWindow<T extends WindowSettings> extends Window implements EcoLabWindow<T> {
    protected T settings;

    @PostConstruct
    protected abstract void init();

    public void show(T settings) {
        if (!UI.getCurrent().getWindows().contains(this)) {
            this.settings = settings;
            clear();
            beforeShow();
            center();
            UI.getCurrent().addWindow(this);
        }
    }

    /**
     * Действия, выполняемые перед отображением окна
     */
    protected void beforeShow() {

    }

    /**
     * Очистка компонента
     */
    protected void clear() {

    }
}
