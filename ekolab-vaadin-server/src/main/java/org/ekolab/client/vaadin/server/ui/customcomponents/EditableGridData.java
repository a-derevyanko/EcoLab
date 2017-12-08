package org.ekolab.client.vaadin.server.ui.customcomponents;

import java.util.List;
import java.util.Objects;

public class EditableGridData<T> {
    private final int number;

    private final List<T> values;

    public EditableGridData(int number, List<T> values) {
        this.number = number;
        this.values = values;
    }

    public T getValue(int index) {
        return values.get(index);
    }

    public T setValue(int index, T value) {
        return values.set(index, value);
    }

    public int getNumber() {
        return number;
    }

    public List<T> getValues() {
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EditableGridData)) return false;
        EditableGridData<?> that = (EditableGridData<?>) o;
        return number == that.number &&
                Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, System.identityHashCode(values));
    }
}
