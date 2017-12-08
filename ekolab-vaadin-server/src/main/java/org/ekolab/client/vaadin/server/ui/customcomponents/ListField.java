package org.ekolab.client.vaadin.server.ui.customcomponents;

import com.vaadin.data.Converter;
import com.vaadin.data.HasValue;
import com.vaadin.shared.Registration;
import org.ekolab.client.vaadin.server.service.impl.I18N;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListField<T> extends EditableGrid<T> implements HasValue<List<T>> {
    private final T defaultValue;
    public ListField(T defaultValue) {
        this.defaultValue = defaultValue;
        setWidth(100.0F, Unit.PERCENTAGE);
        setHeightByRows(1.0);
    }

    public void createColumns(I18N i18N, Converter<String, T> converter, List<String> captions) {
        createColumns(i18N, null, captions, converter);
        setRowCount(1, defaultValue);
    }

    @Override
    public void setValue(List<T> value) {
        if (value == null) {
            value = new ArrayList<>(Collections.nCopies(getColumns().size(), defaultValue));
        }
        setItems(Collections.singleton(new EditableGridData<>(0, value)));
    }

    @Override
    public List<T> getValue() {
        return getItems().get(0).getValues();
    }

    @Override
    public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return false;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        getEditor().setEnabled(!readOnly);
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
