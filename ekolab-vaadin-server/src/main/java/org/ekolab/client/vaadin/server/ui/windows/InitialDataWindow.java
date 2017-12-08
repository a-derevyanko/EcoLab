package org.ekolab.client.vaadin.server.ui.windows;

import com.google.common.collect.Lists;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.StreamResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;
import org.apache.commons.io.FilenameUtils;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.common.DownloadStreamResource;
import org.ekolab.client.vaadin.server.ui.common.UIUtils;
import org.ekolab.client.vaadin.server.ui.customcomponents.ListField;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.server.common.Profiles;
import org.ekolab.server.model.content.DataValue;
import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.LabVariant;
import org.ekolab.server.service.api.content.LabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 777Al on 20.04.2017.
 */
@SpringComponent
@UIScope
@Profile(Profiles.ADDITIONS.EMAIL_NOT_ACTIVE)
public class InitialDataWindow<T extends LabData<V>, V extends LabVariant> extends BaseEkoLabWindow<InitialDataWindow.InitialDataWindowSettings<T, V>> {

    // ---------------------------- Графические компоненты --------------------
    protected final VerticalLayout content = new VerticalLayout();
    protected final HorizontalLayout topLayout = new HorizontalLayout();
    protected final HorizontalLayout imagesLayout = new HorizontalLayout();
    protected final Button printDataButton = new Button("Print initial data", VaadinIcons.PRINT);
    protected final Grid<DataValue> valuesGrid = new Grid<>();
    protected final VerticalLayout additionalFields = new VerticalLayout();

    protected final I18N i18N;

    @Autowired
    public InitialDataWindow(I18N i18N) {
        this.i18N = i18N;
    }

    @PostConstruct
    protected void init() {
        setCaption(i18N.get("labwizard.initial-data"));
        setContent(content);
        setHeight(80.0F, Unit.PERCENTAGE);
        setWidth(80.0F, Unit.PERCENTAGE);
        content.setSizeFull();
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        content.addComponent(topLayout);
        content.addComponent(imagesLayout);
        content.addComponent(valuesGrid);
        content.addComponent(additionalFields);
        content.setExpandRatio(valuesGrid, 1.0F);
        topLayout.addComponent(printDataButton);
        imagesLayout.setSpacing(true);
        imagesLayout.setWidth(100.0F, Unit.PERCENTAGE);
        imagesLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        printDataButton.setCaption(i18N.get("labwizard.initial-data-print"));
        printDataButton.setStyleName(EkoLabTheme.BUTTON_PRIMARY);

        valuesGrid.setSizeFull();
        valuesGrid.addColumn(DataValue::getName, new HtmlRenderer()).setCaption(i18N.get("report.lab-data.parameter-name")).setExpandRatio(4);
        valuesGrid.addColumn(DataValue::getSign, new HtmlRenderer()).setCaption(i18N.get("report.lab-data.parameter-sign")).setExpandRatio(1);
        valuesGrid.addColumn(DataValue::getValue).setCaption(i18N.get("report.lab-data.parameter-value")).setExpandRatio(1);
        valuesGrid.addColumn(DataValue::getDimension, new HtmlRenderer()).setCaption(i18N.get("report.lab-data.parameter-dimension")).setExpandRatio(1);

        additionalFields.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        new BrowserWindowOpener(new DownloadStreamResource(
                () -> settings.labService.printInitialData(settings.variant, UI.getCurrent().getLocale()),
                "initialData.pdf")).extend(printDataButton);
        center();
    }

    @Override
    protected void beforeShow() {
        Set<DataValue> values = new LinkedHashSet<>();

        Map<String, URL> images = new LinkedHashMap<>();

        List<Component> additionalComponents = new ArrayList<>();

        for (DataValue dataValue : settings.labService.getInitialDataValues(settings.variant, UI.getCurrent().getLocale())) {
            // Т. к. Grid до сих пор не поддерживает ячейки из нескольких строк, заменяем на пробелы все переходы на новую строку
            dataValue.setName(dataValue.getName().replaceAll("<br>", " "));
            if (dataValue.getValue() instanceof URL) {
                images.put(dataValue.getName(), (URL) dataValue.getValue());
            } else if (dataValue.getValue() instanceof Map) {
                ListField<?> field = new ListField<>(null);
                Map<String, ?> val = (Map) dataValue.getValue();
                field.createColumns(i18N, UIUtils.getStringConverter((Class) val.values().iterator().next().getClass(), i18N),
                        Lists.newArrayList(val.keySet()));
                field.setValue(Lists.newArrayList(((Map) val).values()));
                field.setReadOnly(true);
                Label caption = new Label(dataValue.getName());
                caption.setStyleName(EkoLabTheme.LABEL_LARGE);
                additionalComponents.add(caption);
                additionalComponents.add(field);
            } else {
                values.add(dataValue);
            }
        }

        if (images.isEmpty()) {
            imagesLayout.setVisible(false);
        } else {
            for (Map.Entry<String, URL> entry : images.entrySet()) {
                Component component = new com.vaadin.ui.Image(entry.getKey(), new StreamResource((StreamResource.StreamSource) () -> {
                    try {
                        return entry.getValue().openStream();
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }, FilenameUtils.getName(entry.getValue().getFile())));
                component.setHeight(180.0F, Unit.PIXELS);
                imagesLayout.addComponent(component);
            }
        }
        valuesGrid.setItems(values);
        additionalComponents.forEach(additionalFields::addComponent);
    }

    @Override
    protected void clear() {
        super.clear();
        imagesLayout.removeAllComponents();
        valuesGrid.setItems(Collections.emptyList());
        additionalFields.removeAllComponents();
    }

    public static class InitialDataWindowSettings<T extends LabData<V>, V extends LabVariant> implements WindowSettings {
        private final V variant;

        private final LabService<T, V> labService;

        public InitialDataWindowSettings(V variant, LabService<T, V> labService) {
            this.variant = variant;
            this.labService = labService;
        }

        public V getVariant() {
            return variant;
        }

        public LabService<T, V> getLabService() {
            return labService;
        }
    }
}
