package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.data.Binder;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.client.vaadin.server.ui.windows.InitialDataWindow;
import org.ekolab.server.common.Profiles;
import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.LabVariant;
import org.ekolab.server.service.api.content.LabMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSendException;

import javax.annotation.PostConstruct;

/**
 * Created by 777Al on 20.04.2017.
 */
@SpringComponent
@UIScope
@Profile(Profiles.ADDITIONS.EMAIL)
public class InitialDataWindowWithEmail<T extends LabData<V>, V extends LabVariant> extends InitialDataWindow<T, V> {
    private final Button sendDataButton = new Button("Send initial data to email", VaadinIcons.AT);
    private final TextField emailField = new TextField();

    @Autowired
    private final LabMailService labMailService;

    public InitialDataWindowWithEmail(LabMailService labMailService) {
        this.labMailService = labMailService;
    }

    @PostConstruct
    protected void init() {
        super.init();
        topLayout.addComponents(emailField, sendDataButton);

        new Binder<String>().forField(emailField).
                withValidator(new EmailValidator(i18N.get("labwizard.initial-data-email-not-valid"))).
                withValidationStatusHandler(statusChange -> sendDataButton.setEnabled(!statusChange.isError())).
                bind(s -> s, (s, v) -> s = v);
        emailField.setPlaceholder(i18N.get("labwizard.initial-data-email"));

        sendDataButton.setCaption(i18N.get("labwizard.initial-data-email-send"));
        sendDataButton.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
        sendDataButton.setEnabled(false);
        sendDataButton.addClickListener(event -> {
            try {
                labMailService.sentInitialDataToEmail(settings.getLabService().printInitialData(settings.getVariant(), getLocale()), UI.getCurrent().getLocale(), emailField.getValue());
                Notification.show(i18N.get("labwizard.initial-data-email-success", emailField.getValue()), Notification.Type.HUMANIZED_MESSAGE);
            } catch (MailSendException e) {
                Notification.show(i18N.get("labwizard.initial-data-email-error", emailField.getValue()), Notification.Type.ERROR_MESSAGE);
            }
        });
        center();
    }
}
