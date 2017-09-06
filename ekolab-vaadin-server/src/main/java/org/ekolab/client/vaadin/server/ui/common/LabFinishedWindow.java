package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.server.model.content.LabData;
import org.ekolab.server.service.api.content.LabService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;

/**
 * Created by 777Al on 20.04.2017.
 */
@SpringComponent
@UIScope
public class LabFinishedWindow<T extends LabData<?>> extends Window {

    // ---------------------------- Графические компоненты --------------------
    private final VerticalLayout content = new VerticalLayout();
    private final Button saveReportButton = new Button("Save report", VaadinIcons.DOWNLOAD);

    private T labData;

    private LabService<T> labService;

    @Autowired
    private I18N i18N;

    @PostConstruct
    public void init() {
        setCaption(i18N.get("labwizard.initial-data"));
        setContent(content);
        setHeight(50.0F, Unit.PERCENTAGE);
        setWidth(50.0F, Unit.PERCENTAGE);
        setSizeFull();
        content.setMargin(true);
        content.addComponent(saveReportButton);
        content.setComponentAlignment(saveReportButton, Alignment.MIDDLE_CENTER);

        saveReportButton.setCaption(i18N.get("labwizard.save-report"));

        saveReportButton.addStyleName(EkoLabTheme.BUTTON_PRIMARY);
        saveReportButton.addStyleName(EkoLabTheme.BUTTON_TINY);

        FileDownloader fileDownloader = new FileDownloader(new StreamResource(() ->
                new ByteArrayInputStream(labService.createReport(labData, UI.getCurrent().getLocale())),
                "report.pdf"));

        fileDownloader.extend(saveReportButton);
        center();
    }

    public void show(T labData, LabService<T> labService) {
        this.labData = labData;
        this.labService = labService;
        if (!UI.getCurrent().getWindows().contains(this)) {
            UI.getCurrent().addWindow(this);
        }
    }
}
