package org.ecolab.client.vaadin.server.ui.customcomponents;

import com.vaadin.data.Binder;
import com.vaadin.data.Converter;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.view.api.UIComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EditableGrid<T> extends Grid<EditableGridData<T>> implements UIComponent {
    public EditableGrid() {
        setWidth(100.0F, Unit.PERCENTAGE);
        setDataProvider(DataProvider.ofCollection(Collections.emptyList()));
        getEditor().setEnabled(true);
    }

    public void createColumns(I18N i18N, String numberCaption, List<String> captions, Converter<String, T> converter) {
        getEditor().setSaveCaption(i18N.get("savable.save"));
        getEditor().setCancelCaption(i18N.get("editable.cancel"));
        if (numberCaption != null) {
            addColumn(EditableGridData::getCaption).setCaption(numberCaption);
        }

        IntStream.range(0, captions.size()).forEachOrdered(i -> {
            var caption = captions.get(i);
            var field = new TextField();
            var binder = getEditor().getBinder();
            var binding = binder.forField(field).
                    withConverter(converter).
                    bind(data -> data.getValue(i), (doubleData, aDouble) -> doubleData.setValue(i, aDouble));
            var column = addColumn(data -> data.getValue(i)).setCaption(caption).setSortable(false);
            column.setEditorBinding(binding);
        });
    }

    public void setRowCount(int rows, T defaultValue) {
        var items = getDataProvider().getItems().stream().limit(rows).collect(Collectors.toList());

        IntStream.rangeClosed(items.size() + 1, rows).forEachOrdered(i -> {
            var data = new EditableGridData<T>(i, new ArrayList<>(Collections.nCopies(getColumns().size(), defaultValue)));
            items.add(data);
        });
        setItems(items);
    }

    @Override
    public ListDataProvider<EditableGridData<T>> getDataProvider() {
        return (ListDataProvider<EditableGridData<T>>) super.getDataProvider();
    }
}
