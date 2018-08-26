package org.ecolab.client.vaadin.server.ui.windows;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FileDownloader;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.EcoLabNavigator;
import org.ecolab.client.vaadin.server.ui.common.DownloadStreamResource;
import org.ecolab.client.vaadin.server.ui.styles.EcoLabTheme;
import org.ecolab.client.vaadin.server.ui.view.LabChooserView;
import org.ecolab.server.common.Profiles;
import org.ecolab.server.model.content.LabData;
import org.ecolab.server.model.content.LabVariant;
import org.ecolab.server.service.api.content.LabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

/**
 * Created by 777Al on 20.04.2017.
 */
@SpringComponent
@UIScope
@Profile({Profiles.MODE.DEV, Profiles.MODE.PROD})
public class LabFinishedWindow<T extends LabData<V>, V extends LabVariant> extends BaseEcoLabWindow<LabFinishedWindow.LabFinishedWindowSettings<T, V>> {

    // ---------------------------- Графические компоненты --------------------
    private final VerticalLayout content = new VerticalLayout();
    private final VerticalLayout buttons = new VerticalLayout();
    private final Button saveReportButton = new Button("Save report", VaadinIcons.DOWNLOAD);
    private final Button toMainMenuButton = new Button("Exit to main menu", VaadinIcons.EXIT);

    private final I18N i18N;

    private final EcoLabNavigator navigator;

    @Autowired
    public LabFinishedWindow(I18N i18N, EcoLabNavigator navigator) {
        this.i18N = i18N;
        this.navigator = navigator;
    }

    @PostConstruct
    public void init() {
        setCaption(i18N.get("labwizard.lab-finished"));
        setContent(content);
        setHeight(80.0F, Unit.PERCENTAGE);
        setWidth(80.0F, Unit.PERCENTAGE);
        setModal(true);
        content.setStyleName(EcoLabTheme.PANEL_LAB_FINISHED);
        content.setSizeFull();
        content.setMargin(true);
        content.addComponent(buttons);
        content.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);
        buttons.setDefaultComponentAlignment(Alignment.BOTTOM_RIGHT);
        buttons.addComponent(saveReportButton);
        buttons.addComponent(toMainMenuButton);

        saveReportButton.setCaption(i18N.get("labwizard.report.save-report"));

        saveReportButton.addStyleName(EcoLabTheme.BUTTON_PRIMARY);
        saveReportButton.addStyleName(EcoLabTheme.BUTTON_TINY);

        toMainMenuButton.setCaption(i18N.get("labwizard.lab-finished.go-to-main-menu"));

        toMainMenuButton.addStyleName(EcoLabTheme.BUTTON_PRIMARY);
        toMainMenuButton.addStyleName(EcoLabTheme.BUTTON_TINY);

        FileDownloader fileDownloader = new FileDownloader(
                new DownloadStreamResource(() -> settings.labService.createReport(settings.labData, UI.getCurrent().getLocale()), "report.pdf"));
        fileDownloader.extend(saveReportButton);

        toMainMenuButton.addClickListener(event -> close());
        center();
    }

    @Override
    public void close() {
        super.close();
        navigator.redirectToView(LabChooserView.NAME);
    }

    public static class LabFinishedWindowSettings<T extends LabData<V>, V extends LabVariant> implements WindowSettings {
        private final T labData;

        private final LabService<T, V> labService;

        public LabFinishedWindowSettings(T labData, LabService<T, V> labService) {
            this.labData = labData;
            this.labService = labService;
        }


        public T getLabData() {
            return labData;
        }

        public LabService<T, V> getLabService() {
            return labService;
        }
    }
}
