package org.ecolab.client.vaadin.server.ui.windows;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.customcomponents.ComponentErrorNotification;
import org.ecolab.client.vaadin.server.ui.styles.EcoLabTheme;
import org.ecolab.server.model.DomainModel;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class NewItemWindow<V, T extends DomainModel> extends BaseEcoLabWindow<NewItemWindow.NewItemWindowSettings<V, T>> {
    // ---------------------------- Графические компоненты --------------------
    protected final Button save = new Button("Ok", event -> save());
    protected final Button cancel = new Button("Cancel", event -> close());

    protected final HorizontalLayout actions = new HorizontalLayout(save, cancel);
    protected final VerticalLayout content = new VerticalLayout(actions);

    // ------------------------------------ Данные экземпляра -------------------------------------------
    protected final I18N i18N;

    protected NewItemWindow(I18N i18N) {
        this.i18N = i18N;
    }

    @PostConstruct
    @Override
    protected void init() {
        setContent(content);
        setCaption(i18N.get("new-item-window.new-item"));
        save.setStyleName(EcoLabTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        content.setSizeUndefined();
        content.setMargin(true);

        actions.setSpacing(true);
        cancel.setCaption(i18N.get("confirm.cancel"));
    }

    protected void save() {
        try {
            T savedItem = settings.saveFunction.apply(getValue());
            settings.valueConsumer.accept(savedItem);
            close();
        } catch (RuntimeException ex) {
            if (settings.existPredicate.test(getValue())) {
                ComponentErrorNotification.show(i18N.get("savable.save-exception-caption"), i18N.get("savable.exist-exception"));
            } else {
                ComponentErrorNotification.show(i18N.get("savable.save-exception-caption"), i18N.get("savable.save-exception"));
            }
        }
    }

    protected abstract V getValue();

    public static class NewItemWindowSettings<VALUETYPE, TYPE extends DomainModel> implements WindowSettings {
        private final Consumer<TYPE> valueConsumer;

        private final Predicate<VALUETYPE> existPredicate;

        private final Function<VALUETYPE, TYPE> saveFunction;

        public NewItemWindowSettings(Consumer<TYPE> valueConsumer, Predicate<VALUETYPE> existPredicate, Function<VALUETYPE, TYPE> saveFunction) {
            this.valueConsumer = valueConsumer;
            this.existPredicate = existPredicate;
            this.saveFunction = saveFunction;
        }
    }
}
