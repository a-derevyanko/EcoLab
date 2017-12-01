package org.ekolab.client.vaadin.server.ui.customcomponents;

import com.vaadin.data.HasValue;
import com.vaadin.data.ValueProvider;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Grid;
import com.vaadin.ui.components.grid.ItemClickListener;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class ListField<T> extends Grid<List<T>> implements HasValue<List<T>> {
    public ListField(int size) {
        IntStream.range(0, size).mapToObj(i -> (ValueProvider<List<T>, T>) ts -> ts.get(i)).forEach(this::addColumn);
    }

    @Override
    public void setValue(List<T> value) {
        setItems(Collections.singleton(value));
    }

    @Override
    public List<T> getValue() {
        return getSelectedItems().iterator().next();
    }

    @Override
    public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return false;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isReadOnly() {
        return isEnabled();
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<List<T>> listener) {
        return addItemClickListener((ItemClickListener<List<T>>) event -> listener.valueChange(new ValueChangeEvent<List<T>>(event.getSource(), ListField.this, getValue(), true)));
    }
}
