package org.ekolab.client.vaadin.server.ui.customcomponents;

import java.util.List;

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
}
