package org.ecolab.client.vaadin.server.ui.view.content.lab_2;

import com.vaadin.data.Binder;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ecolab.client.vaadin.server.ui.customcomponents.EditableGridData;
import org.ecolab.server.model.content.lab2.CalculationResultType;
import org.ecolab.server.model.content.lab2.Frequency;
import org.ecolab.server.model.content.lab2.Lab2Data;
import org.ecolab.server.model.content.lab2.Lab2Variant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public abstract class Lab2Step3<V extends Lab2Variant> extends VerticalLayout implements LabWizardStep<Lab2Data<V>, V> {
    private final I18N i18N;

    private final Binder<Lab2Data<V>> binder;

    // ----------------------------- Графические компоненты --------------------------------
    private final Grid<Data> resultGrid = new Grid<>();

    protected Lab2Step3(I18N i18N,
                     Binder<Lab2Data<V>> binder) {
        this.i18N = i18N;
        this.binder = binder;
    }

    @Override
    public void init() {
        LabWizardStep.super.init();
        setSizeFull();
        setMargin(true);
        addComponent(resultGrid);

        resultGrid.setSizeFull();
        resultGrid.setCaptionAsHtml(true);
        resultGrid.setCaption(i18N.get("lab2.step3.general-data"));
        resultGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        Grid.Column<Data, ?> nColumn = resultGrid.addColumn(Data::getCaption).setCaption(i18N.get("lab2.step3.table-column-caption"));
        Grid.Column<Data, ?> nameColumn = resultGrid.addColumn(Data::getName).setCaption(i18N.get("lab2.step3.table-column-name"));
        Grid.Column<Data, ?> signColumn = resultGrid.addColumn(Data::getSign, new HtmlRenderer()).setCaption(i18N.get("lab2.step3.table-column-sign"));
        Grid.Column<Data, ?> dimensionColumn = resultGrid.addColumn(Data::getDimension).setCaption(i18N.get("lab2.step3.table-column-dimension"));

        Grid.Column[] valuesColumns = Arrays.stream(Frequency.values()).map(frequency -> resultGrid.addColumn(
                data -> data.getValue(frequency.ordinal())).setCaption(i18N.get(frequency))).toArray(Grid.Column[]::new);

        /*
        HeaderRow groupingHeader = resultGrid.prependHeaderRow();
        HeaderCell valuesCell = groupingHeader.join(valuesColumns);

        valuesCell.setText(i18N.get("lab2.step3.table-values-caption"));
        */
    }

    @Override
    public void beforeEnter() {
        AtomicInteger i = new AtomicInteger();
        resultGrid.setItems(Arrays.stream(CalculationResultType.values()).
                map(type -> new Data(i.incrementAndGet(), binder.getBean().getCalculationResult().containsKey(type) ?
                        binder.getBean().getCalculationResult().get(type) : Collections.nCopies(9, 0.0),
                        i18N.get("lab2.step3.name." + type.name()),
                        i18N.get("lab2.step3.sign." + type.name()),
                        i18N.get("lab2.step3.dimension." + type.name()))).
                collect(Collectors.toList()));
    }

    private class Data extends EditableGridData<Double> {
        private final String name;
        private final String sign;
        private final String dimension;

        public Data(int caption, List<Double> values, String name, String sign, String dimension) {
            super(caption, values);
            this.name = name;
            this.sign = sign;
            this.dimension = dimension;
        }

        public String getName() {
            return name;
        }

        public String getSign() {
            return sign;
        }

        public String getDimension() {
            return dimension;
        }
    }
}
