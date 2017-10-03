package org.ekolab.client.vaadin.server.ui.view.content.lab_3;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.server.model.content.lab3.Lab3Data;
import org.ekolab.server.service.api.content.lab3.Lab3ChartType;
import org.ekolab.server.service.api.content.lab3.Lab3Service;
import org.jfree.chart.JFreeChart;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addon.JFreeChartWrapper;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringComponent
@ViewScope
public class Lab3Step5 extends GridLayout implements LabWizardStep {
    // ----------------------------- Графические компоненты --------------------------------
    private final Label chartTypeLabel = new Label("Chart type");
    private final ComboBox<Lab3ChartType> chartType = new ComboBox<>(null, Arrays.asList(Lab3ChartType.values()));
    private final HorizontalLayout chartTypeLayout = new HorizontalLayout(chartTypeLabel, chartType);

    private JFreeChartWrapper chart;

    @Autowired
    private I18N i18N;

    @Autowired
    private Binder<Lab3Data> dataBinder;

    @Autowired
    private Lab3Service lab3Service;

    @Override
    public void init() throws IOException {
        LabWizardStep.super.init();
        setSizeFull();
        setMargin(true);
        setRows(20);
        setColumns(20);

        chartTypeLayout.setComponentAlignment(chartTypeLabel, Alignment.MIDDLE_RIGHT);

        chartTypeLabel.setStyleName(EkoLabTheme.LABEL_BOLD);
        chartTypeLabel.setValue(i18N.get("lab3.step5.chart-type"));

        chartType.setItemCaptionGenerator((ItemCaptionGenerator<Lab3ChartType>) item -> i18N.get("lab3.step5." + item.name().toLowerCase()));
        chartType.setWidth(170F, Unit.PIXELS);
        chartType.setTextInputAllowed(false);
        chartType.setEmptySelectionAllowed(false);
        chartType.addStyleName(EkoLabTheme.COMBOBOX_TINY);
        chartType.addValueChangeListener(event -> {
            if (chart != null) {
                removeComponent(chart);
            }

            JFreeChart isoLineChart = lab3Service.createChart(dataBinder.getBean(), UI.getCurrent().getLocale(), event.getValue());
            if (isoLineChart != null) {
                chart = new JFreeChartWrapper(isoLineChart);
                chart.setSizeFull();
                addComponent(chart, 0, 0, 19, 19);
            }
        });
}

    @Override
    public void placeAdditionalComponents(HorizontalLayout buttonsLayout) {
        buttonsLayout.addComponents(chartTypeLayout);
    }

    @Override
    public void beforeEnter() {
        chartType.setSelectedItem(Lab3ChartType.ISOLINE);
    }
}
