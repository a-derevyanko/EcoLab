package org.ekolab.client.vaadin.server.ui.view;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.NativeButton;
import org.ekolab.client.vaadin.server.service.api.ResourceService;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.EkoLabNavigator;
import org.ekolab.client.vaadin.server.ui.common.BaseEkoLabWindow;
import org.ekolab.client.vaadin.server.ui.common.EkoLabWindow;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Created by 777Al on 03.04.2017.
 */
@SpringComponent
@UIScope
public class LabTypeSelectorWindow extends BaseEkoLabWindow<LabTypeSelectorWindow.LabTypeSelectorWindowSettings> {
    @Autowired
    private final EkoLabNavigator navigator;

    @Autowired
    private final I18N i18N;

    @Autowired
    private final ResourceService resourceService;

    // ---------------------------- Графические компоненты --------------------
    private final NativeButton randomDataButton = new NativeButton("Random data");
    private final NativeButton experimentButton = new NativeButton("Experiment data");
    private final GridLayout labTypeChooserContent = new GridLayout(2, 2);

    public LabTypeSelectorWindow(EkoLabNavigator navigator, I18N i18N, ResourceService resourceService) {
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
        randomDataButton.setStyleName(EkoLabTheme.BUTTON_MULTILINE);
        randomDataButton.addStyleName(EkoLabTheme.BUTTON_VARIANT_CHOOSER);
        randomDataButton.addClickListener(event -> {navigator.navigateTo(settings.randomDataView); close();});

        experimentButton.setCaption(i18N.get("lab.experiment.title"));
        experimentButton.setStyleName(EkoLabTheme.BUTTON_MULTILINE);
        experimentButton.addStyleName(EkoLabTheme.BUTTON_VARIANT_CHOOSER);
        experimentButton.addClickListener(event -> {navigator.navigateTo(settings.experimentDataView); close();});

        Image experimentType = resourceService.getImage(EkoLabTheme.EXPERIMENT_TYPE);
        experimentType.setSizeFull();
        Image randomDataType = resourceService.getImage(EkoLabTheme.RANDOM_DATA_TYPE);
        randomDataType.setSizeFull();

        labTypeChooserContent.setSizeFull();
        labTypeChooserContent.setSpacing(true);
        labTypeChooserContent.setMargin(true);
        labTypeChooserContent.setRowExpandRatio(0, 10.0F);
        labTypeChooserContent.setRowExpandRatio(1, 1.0F);
        labTypeChooserContent.addComponent(randomDataType, 0, 0, 0, 0);
        labTypeChooserContent.addComponent(experimentType, 1, 0, 1, 0);
        labTypeChooserContent.addComponent(randomDataButton, 0, 1, 0, 1);
        labTypeChooserContent.addComponent(experimentButton, 1, 1, 1, 1);
    }

    public static class LabTypeSelectorWindowSettings implements EkoLabWindow.WindowSettings {
        private final String randomDataView;

        private final String experimentDataView;

        public LabTypeSelectorWindowSettings(String randomDataView, String experimentDataView) {
            this.randomDataView = randomDataView;
            this.experimentDataView = experimentDataView;
        }
    }
}
