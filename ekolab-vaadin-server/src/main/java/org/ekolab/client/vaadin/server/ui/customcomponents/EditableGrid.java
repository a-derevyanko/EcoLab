package org.ekolab.client.vaadin.server.ui.customcomponents;

import com.vaadin.data.Binder;
import com.vaadin.data.Converter;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.view.api.UIComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class EditableGrid<T> extends Grid<EditableGridData<T>> implements UIComponent {
    public EditableGrid() {
        getEditor().setEnabled(true);
        getEditor().setSaveCaption(I18N.getInstance().get("savable.save"));
        getEditor().setCancelCaption(I18N.getInstance().get("editable.cancel"));
    }

    public void createColumns(String numberCaption, List<String> captions, Converter<String, T> converter) {
        if (numberCaption != null) {
            addColumn(EditableGridData::getNumber).setCaption(numberCaption);
        }

        IntStream.range(0, captions.size()).forEachOrdered(i -> {
            String caption = captions.get(i);
            TextField field = new TextField();
            Binder<EditableGridData<T>> binder = getEditor().getBinder();
            Binder.Binding<EditableGridData<T>, T> binding = binder.forField(field).
                    withConverter(converter).
                    bind(data -> data.getValue(i), (doubleData, aDouble) -> doubleData.setValue(i, aDouble));
            Column<EditableGridData<T>, T> column = addColumn(data -> data.getValue(i)).setCaption(caption);
            column.setEditorBinding(binding);
        });
    }

    public void setRowCount(int rows, T defaultValue) {
        List<EditableGridData<T>> items = new ArrayList<>(getDataProvider() instanceof ListDataProvider ?
                ((ListDataProvider<EditableGridData<T>>) getDataProvider()).getItems() : Collections.emptyList());

        IntStream.rangeClosed(1, rows).forEachOrdered(i -> {
            EditableGridData<T> data = new EditableGridData<>(i, new ArrayList<>(Collections.nCopies(getColumns().size(), defaultValue)));
            items.add(data);
        });
        setItems(items);
    }
}
