package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import javax.annotation.PostConstruct;

/**
 * Created by 777Al on 03.04.2017.
 */
public abstract class BaseEkoLabWindow<T extends EkoLabWindow.WindowSettings> extends Window implements EkoLabWindow<T> {
    protected T settings;

    @PostConstruct
    protected abstract void init();

    public void show(T settings) {
        if (!UI.getCurrent().getWindows().contains(this)) {
            this.settings = settings;
            clear();
            beforeShow();
            UI.getCurrent().addWindow(this);
        }
    }

    /**
     * Действия, выполняемые перед отображением окна
     */
    protected void beforeShow() {

    };

    /**
     * Очистка компонента
     */
    protected void clear() {

    };
}
