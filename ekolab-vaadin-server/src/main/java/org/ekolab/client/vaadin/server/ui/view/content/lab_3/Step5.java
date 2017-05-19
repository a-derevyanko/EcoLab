package org.ekolab.client.vaadin.server.ui.view.content.lab_3;

import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.service.ResourceService;
import org.ekolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.server.model.content.lab3.Lab3Data;
import org.ekolab.server.service.api.content.lab3.IsoLineChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addon.JFreeChartWrapper;

import java.io.IOException;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringComponent
@ViewScope
public class Step5 extends GridLayout implements LabWizardStep {
    // ----------------------------- Графические компоненты --------------------------------
    private final Button so2MapButton = new Button("SO2 map", VaadinIcons.MAP_MARKER);
    private final Button ashMapButton = new Button("Ash map", VaadinIcons.MAP_MARKER);
    private final Button zoomInButton = new Button(VaadinIcons.PLUS_CIRCLE_O);
    private final Button zoomOutButton = new Button(VaadinIcons.MINUS_CIRCLE_O);

    @Autowired
    private I18N i18N;

    @Autowired
    private Binder<Lab3Data> dataBinder;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private IsoLineChartService isoLineChartService;

    @Override
    public void init() throws IOException {
        LabWizardStep.super.init();
        setSizeFull();
        setMargin(true);
        setRows(20);
        setColumns(20);
        so2MapButton.setSizeFull();
        ashMapButton.setSizeFull();

        so2MapButton.setCaption(i18N.get("lab3.step5.so2-map"));
        so2MapButton.setCaptionAsHtml(true);
        ashMapButton.setCaption(i18N.get("lab3.step5.ash-map"));
        so2MapButton.addStyleName(EkoLabTheme.BUTTON_PRIMARY);
        ashMapButton.addStyleName(EkoLabTheme.BUTTON_PRIMARY);
        zoomInButton.addStyleName(EkoLabTheme.BUTTON_PRIMARY);
        zoomOutButton.addStyleName(EkoLabTheme.BUTTON_PRIMARY);

        JFreeChartWrapper chart = new JFreeChartWrapper(isoLineChartService.
                createIsoLineChart(dataBinder.getBean(), UI.getCurrent().getLocale()));
        chart.setSizeFull();
        addComponent(chart, 0, 0, 19, 19);
}

    @Override
    public void placeAdditionalComponents(HorizontalLayout buttonsLayout) {
        buttonsLayout.addComponents(zoomInButton, zoomOutButton, so2MapButton, ashMapButton);
    }
}
