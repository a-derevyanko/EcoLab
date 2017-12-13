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
        setValue(value, false);
    }

    @Override
    public List<T> getValue() {
        return getDataProvider().getItems().iterator().next().getValues();
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
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        getEditor().setEnabled(enabled);
    }

    @Override
    public boolean isReadOnly() {
        return getEditor().isEnabled();
    }

    @Override
    public Registration addValueChangeListener(ValueChangeListener<List<T>> listener) {
        Registration r1 = getEditor().addSaveListener(
                event -> listener.valueChange(
                        new ValueChangeEvent<>(ListField.this, ListField.this, getValue(), true)));
        Registration r2 = addListener(ValueChangeEvent.class, listener,
                ValueChangeListener.VALUE_CHANGE_METHOD);
        return () -> {
            r1.remove();
            r2.remove();
        };
    }

    private boolean setValue(List<T> value, boolean userOriginated) {
        if (value == null) {
            value = new ArrayList<>(Collections.nCopies(getColumns().size(), defaultValue));
        }
        if (userOriginated && isReadOnly()) {
            return false;
        }
        List<T> oldValue = getValue();
        if (value.equals(getValue())) {
            return false;
        }
        setItems(Collections.singleton(new EditableGridData<>(0, value)));
        fireEvent(new ValueChangeEvent<>(this, oldValue, userOriginated));

        return true;
    }
}
