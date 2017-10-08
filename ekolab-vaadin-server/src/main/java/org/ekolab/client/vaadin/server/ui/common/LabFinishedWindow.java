package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileDownloader;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.EkoLabNavigator;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.client.vaadin.server.ui.view.LabChooserView;
import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.LabVariant;
import org.ekolab.server.service.api.content.LabService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Created by 777Al on 20.04.2017.
 */
@SpringComponent
@UIScope
public class LabFinishedWindow<T extends LabData<V>, V extends LabVariant> extends Window {

    // ---------------------------- Графические компоненты --------------------
    private final VerticalLayout content = new VerticalLayout();
    private final VerticalLayout buttons = new VerticalLayout();
    private final Button saveReportButton = new Button("Save report", VaadinIcons.DOWNLOAD);
    private final Button toMainMenuButton = new Button("Exit to main menu", VaadinIcons.EXIT);

    private T labData;

    private LabService<T, V> labService;

    @Autowired
    private I18N i18N;

    @Autowired
    private EkoLabNavigator navigator;

    @PostConstruct
    public void init() {
        setCaption(i18N.get("labwizard.lab-finished"));
        setContent(content);
        setHeight(80.0F, Unit.PERCENTAGE);
        setWidth(80.0F, Unit.PERCENTAGE);
        setModal(true);
        content.setStyleName(EkoLabTheme.PANEL_LAB_FINISHED);
        content.setSizeFull();
        content.setMargin(true);
        content.addComponent(buttons);
        content.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);
        buttons.setDefaultComponentAlignment(Alignment.BOTTOM_RIGHT);
        buttons.addComponent(saveReportButton);
        buttons.addComponent(toMainMenuButton);

        saveReportButton.setCaption(i18N.get("labwizard.report.save-report"));

        saveReportButton.addStyleName(EkoLabTheme.BUTTON_PRIMARY);
        saveReportButton.addStyleName(EkoLabTheme.BUTTON_TINY);

        toMainMenuButton.setCaption(i18N.get("labwizard.lab-finished.go-to-main-menu"));

        toMainMenuButton.addStyleName(EkoLabTheme.BUTTON_PRIMARY);
        toMainMenuButton.addStyleName(EkoLabTheme.BUTTON_TINY);

        FileDownloader fileDownloader = new FileDownloader(
                new DownloadStreamResource(() -> labService.createReport(labData, UI.getCurrent().getLocale()), "report.pdf"));
        fileDownloader.extend(saveReportButton);

        toMainMenuButton.addClickListener(event -> close());
        center();
    }

    public void show(T labData, LabService<T, V> labService) {
        this.labData = labData;
        this.labService = labService;
        if (!UI.getCurrent().getWindows().contains(this)) {
            UI.getCurrent().addWindow(this);
        }
    }

    @Override
    public void close() {
        super.close();
        navigator.redirectToView(LabChooserView.NAME);
    }
}
