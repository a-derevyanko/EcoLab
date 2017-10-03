package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.data.Binder;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.StreamResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.renderers.HtmlRenderer;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.server.model.content.LabVariant;
import org.ekolab.server.service.api.content.LabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
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
public class InitialDataWindow extends Window {

    // ---------------------------- Графические компоненты --------------------
    private final VerticalLayout content = new VerticalLayout();
    private final HorizontalLayout topLayout = new HorizontalLayout();
    private final Button printDataButton = new Button("Print initial data", VaadinIcons.PRINT);
    private final Button sendDataButton = new Button("Send initial data to email", VaadinIcons.AT);
    private final TextField emailField = new TextField();
    private final Grid<Map.Entry<String, String>> valuesGrid = new Grid<>();

    private LabVariant variant;

    private LabService<?> labService;

    @Autowired
    private I18N i18N;

    @PostConstruct
    public void init() {
        setCaption(i18N.get("labwizard.initial-data"));
        setContent(content);
        setHeight(80.0F, Unit.PERCENTAGE);
        setWidth(80.0F, Unit.PERCENTAGE);
        content.setSizeFull();
        content.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        content.addComponent(topLayout);
        content.addComponent(valuesGrid);
        content.setExpandRatio(valuesGrid, 1.0F);
        topLayout.addComponents(printDataButton, emailField, sendDataButton);
        printDataButton.setCaption(i18N.get("labwizard.initial-data-print"));
        printDataButton.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
        sendDataButton.setCaption(i18N.get("labwizard.initial-data-email-send"));
        sendDataButton.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
        sendDataButton.setEnabled(false);

        new Binder<String>().forField(emailField).
                withValidator(new EmailValidator(i18N.get("labwizard.initial-data-email-not-valid"))).
                withValidationStatusHandler(statusChange -> sendDataButton.setEnabled(!statusChange.isError())).
                bind(s -> s, (s, v) -> s = v);
        emailField.setPlaceholder(i18N.get("labwizard.initial-data-email"));

        valuesGrid.setSizeFull();
        valuesGrid.addColumn(Map.Entry::getKey, new HtmlRenderer()).setCaption(i18N.get("labwizard.initial-data-key")).setExpandRatio(1);
        valuesGrid.addColumn(Map.Entry::getValue).setCaption(i18N.get("labwizard.initial-data-value"));

        new BrowserWindowOpener(new StreamResource(() ->
                new ByteArrayInputStream(labService.printInitialData(variant, UI.getCurrent().getLocale())),
                "initialData.pdf")).extend(printDataButton);

        sendDataButton.addClickListener(event -> {
            try {
                labService.sentInitialDataToEmail(variant, UI.getCurrent().getLocale(), "777alterego777545gmail.com");
                Notification.show(i18N.get("labwizard.initial-data-email-success", emailField.getValue()), Notification.Type.HUMANIZED_MESSAGE);
            } catch (MailSendException e) {
                Notification.show(i18N.get("labwizard.initial-data-email-error", emailField.getValue()), Notification.Type.ERROR_MESSAGE);
            }
        });
        center();
    }

    public void show(LabVariant variant, LabService<?> labService) {
        this.variant = variant;
        this.labService = labService;
        if (!UI.getCurrent().getWindows().contains(this)) {
            Map<String, String> values = new HashMap<>();
            for (Field field : variant.getClass().getDeclaredFields()) {
                ReflectionUtils.makeAccessible(field);

                Object value = ReflectionUtils.getField(field, variant);
                String name = i18N.get(field.getName());
                // Т. к. Grid до сих пор не поддерживает ячейки из нескольких строк, заменяем на пробелы все переходы на новую строку
                values.put(name.replaceAll("<br>", " "), value.getClass().isEnum() ? i18N.get(value.getClass().getSimpleName()
                        + '.' + ((Enum<?>) value).name()) : String.valueOf(ReflectionUtils.getField(field, variant)));
            }
            valuesGrid.setItems(values.entrySet());
            UI.getCurrent().addWindow(this);
        }
    }
}
