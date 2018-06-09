package org.ekolab.client.vaadin.server.ui.windows;

import com.vaadin.event.ShortcutAction;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

@SpringComponent
@UIScope
public class NewNamedEntityWindow extends BaseEkoLabWindow<NewNamedEntityWindow.NamedEntityWindowSettings> {
    // ---------------------------- Графические компоненты --------------------
    protected final FormLayout content = new FormLayout();
    protected final Button save = new Button("Save", event -> save());
    protected final Button cancel = new Button("Cancel", event -> close());
    protected final TextField name = new TextField("Name");

    protected final HorizontalLayout actions = new HorizontalLayout(save, cancel);

    // ------------------------------------ Данные экземпляра -------------------------------------------
    protected final I18N i18N;

    protected NewNamedEntityWindow(I18N i18N) {
        this.i18N = i18N;
    }

    @PostConstruct
    public void init() {
        setContent(content);
        save.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        content.setSizeUndefined();
        setResizable(false);
        content.setMargin(true);

        actions.setSpacing(true);

        name.setCaption(i18N.get("entity-name"));
        content.addComponents(name, actions);
        save.setCaption(i18N.get("savable.save"));
        cancel.setCaption(i18N.get("confirm.cancel"));

        center();
    }

    protected void save() {
        settings.consumer.accept(name.getValue());
        close();
    }

    @Override
    protected void beforeShow() {
        super.beforeShow();
        setCaption(settings.caption);
        name.clear();
        name.focus();
    }

    public static class NamedEntityWindowSettings implements WindowSettings {
        protected String caption;
        protected Consumer<String> consumer;

        public NamedEntityWindowSettings(String caption, Consumer<String> consumer) {
            this.caption = caption;
            this.consumer = consumer;
        }
    }
}
