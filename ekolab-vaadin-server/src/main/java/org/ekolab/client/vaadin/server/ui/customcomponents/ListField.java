package org.ekolab.client.vaadin.server.ui.customcomponents;

import com.vaadin.data.Converter;
import com.vaadin.data.HasValue;
import com.vaadin.shared.Registration;

import java.util.Collections;
import java.util.List;

public class ListField<T> extends EditableGrid<T> implements HasValue<List<T>> {
    public ListField(Converter<String, T> converter, List<String> captions) {
        createColumns(null, captions, converter);
    }

    @Override
    public void setValue(List<T> value) {
        setItems(Collections.singleton(new EditableGridData<>(0, value)));
    }

    @Override
    public List<T> getValue() {
        return getSelectedItems().iterator().next().getValues();
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
        return getEditor().addSaveListener(
                event -> listener.valueChange(
                        new ValueChangeEvent<>(ListField.this, ListField.this, getValue(), true)));
    }
}
