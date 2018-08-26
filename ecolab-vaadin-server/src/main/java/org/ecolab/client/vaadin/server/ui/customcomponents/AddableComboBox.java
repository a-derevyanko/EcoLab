package org.ecolab.client.vaadin.server.ui.customcomponents;

import com.vaadin.data.HasItems;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ItemCaptionGenerator;
import org.ecolab.client.vaadin.server.ui.styles.EcoLabTheme;
import org.ecolab.client.vaadin.server.ui.windows.NewItemWindow;
import org.ecolab.server.model.DomainModel;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class AddableComboBox<T extends DomainModel> extends CustomField<T> implements HasItems<T> {
    // ---------------------------- Графические компоненты --------------------
    private final ComboBox<T> comboBox = new ComboBox<>();
    private final Button addButton = new Button(VaadinIcons.PLUS_CIRCLE);

    // ------------------------------------ Данные экземпляра -------------------------------------
    private Supplier<Collection<T>> getItemsCallBack;
    private NewItemWindow newItemWindow;

    public AddableComboBox() {
        addButton.setStyleName(EcoLabTheme.BUTTON_PRIMARY);
        comboBox.setTextInputAllowed(false);
    }

    @Override
    protected Component initContent() {
        HorizontalLayout components = new HorizontalLayout(comboBox, addButton);

        components.setSpacing(true);
        return components;
    }

    public <V> void initSettings(String addButtonDescription, Supplier<Collection<T>> getItemsCallBack,
                                 ItemCaptionGenerator<T> captionGenerator, ValueChangeListener<T> valueChangeListener,
                                 Predicate<V> existPredicate, Function<V, T> saveFunction,
                                 NewItemWindow<V, T> newItemWindow) {
        this.getItemsCallBack = getItemsCallBack;
        comboBox.setItemCaptionGenerator(captionGenerator);
        comboBox.setDescription(addButtonDescription);
        if (valueChangeListener != null) {
            comboBox.addValueChangeListener(valueChangeListener);
        }
        addButton.addClickListener(event -> newItemWindow.show(new NewItemWindow.NewItemWindowSettings<>(o -> {
            refreshItems();
            setValue(o);
        }, existPredicate, saveFunction)));
    }

    @Override
    public DataProvider<T, ?> getDataProvider() {
        return comboBox.getDataProvider();
    }

    @Override
    public void setItems(Collection<T> items) {
        comboBox.setItems(items);
    }

    @Override
    public T getValue() {
        return comboBox.getValue();
    }

    @Override
    protected void doSetValue(T value) {
        comboBox.setValue(value);
    }

    public void refreshItems() {
        setItems(getItemsCallBack.get());
    }
}
