package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.server.model.content.LabVariant;
import org.ekolab.server.service.api.content.LabService;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 777Al on 20.04.2017.
 */
@SpringComponent
@UIScope
public class InitialDataWindow extends Window {
    // ---------------------------- Графические компоненты --------------------
    private final VerticalLayout content = new VerticalLayout();
    private final Button printDataButton = new Button("Print initial data", VaadinIcons.PRINT);
    private final Grid<Map.Entry<String, String>> valuesGrid = new Grid<>();

    @Autowired
    private I18N i18N;

    @PostConstruct
    public void init() {
        setCaption(i18N.get("labwizard.initial-data"));
        setContent(content);
        setHeight(50.0F, Unit.PERCENTAGE);
        setWidth(70.0F, Unit.PERCENTAGE);
        content.setSizeFull();
        content.addComponent(printDataButton);
        content.addComponent(valuesGrid);
        content.setExpandRatio(valuesGrid, 1.0F);
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        content.setComponentAlignment(printDataButton, Alignment.MIDDLE_CENTER);
        printDataButton.setCaption(i18N.get("labwizard.initial-data-print"));
        printDataButton.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
        valuesGrid.setSizeFull();
        valuesGrid.addColumn(Map.Entry::getKey).setCaption(i18N.get("labwizard.initial-data-key"));
        valuesGrid.addColumn(Map.Entry::getValue).setCaption(i18N.get("labwizard.initial-data-value")).setExpandRatio(1);
        center();
    }

    public void show(LabVariant variant, LabService<?> labService) {
        if (!UI.getCurrent().getWindows().contains(this)) {
            Map<String, String> values = new HashMap<>();
            ReflectionUtils.doWithFields(variant.getClass(), field -> {
                field.setAccessible(true);
                values.put(Jsoup.parse(i18N.get(field.getName())).text(), String.valueOf(field.get(variant)));
            });
            valuesGrid.setItems(values.entrySet());
            UI.getCurrent().addWindow(this);

            new ArrayList<>(printDataButton.getExtensions()).forEach(printDataButton::removeExtension);

            new FileDownloader(new StreamResource(() ->
                    new ByteArrayInputStream(labService.printInitialData(values, UI.getCurrent().getLocale())),
                    "initialData.pdf")).extend(printDataButton);
        }
    }
}
