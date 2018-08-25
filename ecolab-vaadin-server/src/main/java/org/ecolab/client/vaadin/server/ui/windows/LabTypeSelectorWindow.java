package org.ecolab.client.vaadin.server.ui.windows;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.NativeButton;
import org.ecolab.client.vaadin.server.service.api.ResourceService;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.EcoLabNavigator;
import org.ecolab.client.vaadin.server.ui.styles.EcoLabTheme;

import javax.annotation.PostConstruct;

/**
 * Created by 777Al on 03.04.2017.
 */
@SpringComponent
@UIScope
public class LabTypeSelectorWindow extends BaseEcoLabWindow<LabTypeSelectorWindow.LabTypeSelectorWindowSettings> {
    private final EcoLabNavigator navigator;

    private final I18N i18N;

    private final ResourceService resourceService;

    // ---------------------------- Графические компоненты --------------------
    private final NativeButton randomDataButton = new NativeButton("Random data");
    private final NativeButton experimentButton = new NativeButton("Experiment data");
    private final GridLayout labTypeChooserContent = new GridLayout(2, 2);
    private final Image experimentType = new Image();
    private final Image randomDataType = new Image();

    public LabTypeSelectorWindow(EcoLabNavigator navigator, I18N i18N, ResourceService resourceService) {
        this.navigator = navigator;
        this.i18N = i18N;
        this.resourceService = resourceService;
    }

    @PostConstruct
    protected void init() {
        setModal(true);
        setWidth(700.0F, Unit.PIXELS);
        setHeight(300.0F, Unit.PIXELS);
        setResizable(false);
        setCaption(i18N.get("lab.choose.title"));
        setContent(labTypeChooserContent);

        randomDataButton.setCaption(i18N.get("lab.random-data.title"));
        randomDataButton.setStyleName(EcoLabTheme.BUTTON_MULTILINE);
        randomDataButton.addStyleName(EcoLabTheme.BUTTON_VARIANT_CHOOSER);
        randomDataButton.addClickListener(event -> {navigator.navigateTo(settings.randomDataView); close();});

        experimentButton.setCaption(i18N.get("lab.experiment.title"));
        experimentButton.setStyleName(EcoLabTheme.BUTTON_MULTILINE);
        experimentButton.addStyleName(EcoLabTheme.BUTTON_VARIANT_CHOOSER);
        experimentButton.addClickListener(event -> {navigator.navigateTo(settings.experimentDataView); close();});

        experimentType.setSource(resourceService.getImage(EcoLabTheme.EXPERIMENT_TYPE));
        experimentType.setSizeFull();
        randomDataType.setSource(resourceService.getImage(EcoLabTheme.RANDOM_DATA_TYPE));
        randomDataType.setSizeFull();

        labTypeChooserContent.setSizeFull();
        labTypeChooserContent.setSpacing(true);
        labTypeChooserContent.setMargin(true);
        labTypeChooserContent.setRowExpandRatio(0, 10.0F);
        labTypeChooserContent.setRowExpandRatio(1, 1.0F);
        labTypeChooserContent.addComponent(randomDataType, 1, 0, 1, 0);
        labTypeChooserContent.addComponent(randomDataButton, 1, 1, 1, 1);
        labTypeChooserContent.addComponent(experimentType, 0, 0, 0, 0);
        labTypeChooserContent.addComponent(experimentButton, 0, 1, 0, 1);
    }

    public static class LabTypeSelectorWindowSettings implements WindowSettings {
        private final String randomDataView;

        private final String experimentDataView;

        public LabTypeSelectorWindowSettings(String randomDataView, String experimentDataView) {
            this.randomDataView = randomDataView;
            this.experimentDataView = experimentDataView;
        }
    }
}
