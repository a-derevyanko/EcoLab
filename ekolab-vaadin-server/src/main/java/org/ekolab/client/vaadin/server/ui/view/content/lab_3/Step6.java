package org.ekolab.client.vaadin.server.ui.view.content.lab_3;

import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.CompositeErrorMessage;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ekolab.server.model.content.lab3.Lab3Data;
import org.ekolab.server.service.api.content.lab3.Lab3Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringComponent
@ViewScope
public class Step6 extends VerticalLayout implements LabWizardStep {
    // ----------------------------- Графические компоненты --------------------------------
    private final Button saveReportButton = new Button("Save report", VaadinIcons.DOWNLOAD);

    @Autowired
    private I18N i18N;

    @Autowired
    private Lab3Service lab3Service;

    @Autowired
    private Binder<Lab3Data> binder;

    @Override
    public void init() throws IOException {
        LabWizardStep.super.init();
        setSizeFull();
        setMargin(true);
        addComponent(saveReportButton);
        saveReportButton.addClickListener(event -> saveReport());
    }

    private void saveReport() {
        FileDownloader fileDownloader = new FileDownloader(new StreamResource(() ->
                new ByteArrayInputStream(lab3Service.createReport(binder.getBean())), "report.pdf"));

        fileDownloader.extend(saveReportButton);
    }

    @Override
    public ErrorMessage getComponentError() {
        Set<ErrorMessage> messageList = Stream.of(super.getComponentError())
                .filter(Objects::nonNull).collect(Collectors.toSet());
        return messageList.isEmpty() ? null : new CompositeErrorMessage(messageList);
    }
}
