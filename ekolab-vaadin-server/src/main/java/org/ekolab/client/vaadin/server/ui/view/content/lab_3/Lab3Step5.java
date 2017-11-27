package org.ekolab.client.vaadin.server.ui.view.content.lab_3;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ItemCaptionGenerator;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ekolab.client.vaadin.server.ui.customcomponents.ComponentErrorNotification;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.server.model.content.lab3.Lab3Data;
import org.ekolab.server.model.content.lab3.Lab3Variant;
import org.ekolab.server.service.api.content.lab3.Lab3ChartType;
import org.ekolab.server.service.api.content.lab3.Lab3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addon.JFreeChartWrapper;

import java.util.Arrays;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringComponent
@ViewScope
public class Lab3Step5 extends GridLayout implements LabWizardStep<Lab3Data, Lab3Variant> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Lab3Step5.class);

    // ----------------------------- Графические компоненты --------------------------------
    private final Label chartTypeLabel = new Label("Chart type");
    private final ComboBox<Lab3ChartType> chartType = new ComboBox<>(null, Arrays.asList(Lab3ChartType.values()));
    private final HorizontalLayout chartTypeLayout = new HorizontalLayout(chartTypeLabel, chartType);

    private JFreeChartWrapper chart;

    private final I18N i18N;

    private final Binder<Lab3Data> dataBinder;

    private final Lab3Service lab3Service;

    @Autowired
    public Lab3Step5(I18N i18N, Binder<Lab3Data> dataBinder, Lab3Service lab3Service) {
        this.i18N = i18N;
        this.dataBinder = dataBinder;
        this.lab3Service = lab3Service;
    }

    @Override
    public void init() {
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

            if (event.getValue() != null) {
                try {
                    chart = new JFreeChartWrapper(lab3Service.createChart(dataBinder.getBean(), UI.getCurrent().getLocale(), event.getValue()));
                    chart.setSizeFull();
                    addComponent(chart, 0, 0, 19, 19);
                } catch (Exception ex) {
                    LOGGER.error(ex.getLocalizedMessage(), ex);
                    ComponentErrorNotification.show(i18N.get("lab3.step5.chart-error"), i18N.get("lab3.step5.chart-error-check-data"));
                }
            }
        });
    }

    @Override
    public void placeAdditionalComponents(HorizontalLayout buttonsLayout) {
        buttonsLayout.addComponents(chartTypeLayout);
    }

    @Override
    public void beforeEnter() {
        chartType.setSelectedItem(null);
        chartType.setSelectedItem(Lab3ChartType.ISOLINE);
    }

    @Override
    public boolean onAdvance() {
        if (chart == null) {
            ComponentErrorNotification.show(i18N.get("lab3.step5.chart-error"), i18N.get("lab3.step5.chart-error-check-data"));
            return false;
        }
        return true;
    }
}
