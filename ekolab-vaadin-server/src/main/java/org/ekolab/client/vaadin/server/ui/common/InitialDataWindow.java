package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.StreamResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.HtmlRenderer;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.server.common.Profiles;
import org.ekolab.server.model.content.LabVariant;
import org.ekolab.server.service.api.content.LabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 777Al on 20.04.2017.
 */
@SpringComponent
@UIScope
@Profile(Profiles.ADDITIONS.EMAIL_NOT_ACTIVE)
public class InitialDataWindow extends BaseEkoLabWindow<InitialDataWindow.InitialDataWindowSettings> {

    // ---------------------------- Графические компоненты --------------------
    protected final VerticalLayout content = new VerticalLayout();
    protected final HorizontalLayout topLayout = new HorizontalLayout();
    protected final Button printDataButton = new Button("Print initial data", VaadinIcons.PRINT);
    protected final Grid<Map.Entry<String, String>> valuesGrid = new Grid<>();

    @Autowired
    protected I18N i18N;

    @PostConstruct
    protected void init() {
        setCaption(i18N.get("labwizard.initial-data"));
        setContent(content);
        setHeight(80.0F, Unit.PERCENTAGE);
        setWidth(80.0F, Unit.PERCENTAGE);
        content.setSizeFull();
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        content.addComponent(topLayout);
        content.addComponent(valuesGrid);
        content.setExpandRatio(valuesGrid, 1.0F);
        topLayout.addComponent(printDataButton);
        printDataButton.setCaption(i18N.get("labwizard.initial-data-print"));
        printDataButton.setStyleName(EkoLabTheme.BUTTON_PRIMARY);

        valuesGrid.setSizeFull();
        valuesGrid.addColumn(Map.Entry::getKey, new HtmlRenderer()).setCaption(i18N.get("labwizard.initial-data-key")).setExpandRatio(1);
        valuesGrid.addColumn(Map.Entry::getValue).setCaption(i18N.get("labwizard.initial-data-value"));

        new BrowserWindowOpener(new StreamResource(() ->
                new ByteArrayInputStream(settings.labService.printInitialData(settings.variant, UI.getCurrent().getLocale())),
                "initialData.pdf")).extend(printDataButton);
        center();
    }

    @Override
    protected void beforeShow() {
        Map<String, String> values = new HashMap<>();
        for (Field field : settings.variant.getClass().getDeclaredFields()) {
            ReflectionUtils.makeAccessible(field);

            Object value = ReflectionUtils.getField(field, settings.variant);
            String name = i18N.get(field.getName());
            // Т. к. Grid до сих пор не поддерживает ячейки из нескольких строк, заменяем на пробелы все переходы на новую строку
            values.put(name.replaceAll("<br>", " "), value.getClass().isEnum() ? i18N.get(value.getClass().getSimpleName()
                    + '.' + ((Enum<?>) value).name()) : String.valueOf(ReflectionUtils.getField(field, settings.variant)));
        }
        valuesGrid.setItems(values.entrySet());

    }

    public static class InitialDataWindowSettings implements EkoLabWindow.WindowSettings {
        private final LabVariant variant;

        private final LabService<?> labService;

        public InitialDataWindowSettings(LabVariant variant, LabService<?> labService) {
            this.variant = variant;
            this.labService = labService;
        }

        public LabVariant getVariant() {
            return variant;
        }

        public LabService<?> getLabService() {
            return labService;
        }
    }
}
