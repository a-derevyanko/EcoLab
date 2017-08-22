package org.ekolab.client.vaadin.server.ui.view;

import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.service.ResourceService;
import org.ekolab.client.vaadin.server.ui.EkoLabNavigator;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.client.vaadin.server.ui.view.api.View;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.Lab1TestView;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.experiment.Lab1ExperimentView;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.random.Lab1RandomDataView;
import org.ekolab.client.vaadin.server.ui.view.content.lab_3.Lab3TestView;
import org.ekolab.client.vaadin.server.ui.view.content.lab_3.Lab3View;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by 777Al on 03.04.2017.
 */
@SpringView(name = LabChooserView.NAME)
public class LabChooserView extends VerticalLayout implements View {
    public static final String NAME = "labchooser";

    @Autowired
    private EkoLabNavigator navigator;

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private I18N i18N;

    // ---------------------------- Графические компоненты --------------------
    private final GridLayout content = new GridLayout(2, 4);
    private final NativeButton lab1Button = new NativeButton("Laboratory work №1");
    private final NativeButton lab2Button = new NativeButton("Laboratory work №2");
    private final NativeButton lab3Button = new NativeButton("Laboratory work №3");
    private final NativeButton labDefenceButton = new NativeButton("Defence of laboratory works");
    private final Button lab1RandomDataButton = new NativeButton("Random data");
    private final Button lab1ExperimentButton = new NativeButton("Experiment data");
    private final Button lab1TestButton = new NativeButton("Lab 1 test");
    private final Button lab2TestButton = new NativeButton("Lab 2 test");
    private final Button lab3TestButton = new NativeButton("Lab 3 test");
    private final GridLayout lab1VariantChooserContent = new GridLayout(2, 2);
    private final GridLayout labTestChooserContent = new GridLayout(1, 3);
    private final Window lab1VariantChooser = new Window("Choose lab 1 variant", lab1VariantChooserContent);
    private final Window labTestChooser = new Window("Choose lab for test", labTestChooserContent);
    private final Label  labLabel = new Label("Environmental technologies at TPPs", ContentMode.HTML);

    @Override
    public void init() throws Exception {
        View.super.init();
        addStyleName(EkoLabTheme.VIEW_LABCHOOSER);
        setMargin(false);
        setSpacing(false);

        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        content.setSizeFull();
        content.setMargin(true);
        content.setSpacing(true);
        Responsive.makeResponsive(content);

        Image logo = resourceService.getImage(EkoLabTheme.IMAGE_LOGO);
        logo.setSizeFull();

        lab1Button.setCaption(i18N.get("lab1.title"));
        lab1Button.setStyleName(EkoLabTheme.BUTTON_MULTILINE);
        lab1Button.addStyleName(EkoLabTheme.BUTTON_CHOOSER);
        lab1Button.addClickListener(event ->  UI.getCurrent().addWindow(lab1VariantChooser));

        lab2Button.setStyleName(EkoLabTheme.BUTTON_MULTILINE);
        lab2Button.addStyleName(EkoLabTheme.BUTTON_CHOOSER);

        lab1RandomDataButton.setCaption(i18N.get("lab1.random-data.title"));
        lab1RandomDataButton.setStyleName(EkoLabTheme.BUTTON_MULTILINE);
        lab1RandomDataButton.addStyleName(EkoLabTheme.BUTTON_VARIANT_CHOOSER);
        lab1RandomDataButton.addClickListener(event -> {navigator.navigateTo(Lab1RandomDataView.NAME); lab1VariantChooser.close();});

        lab1ExperimentButton.setCaption(i18N.get("lab1.experiment.title"));
        lab1ExperimentButton.setStyleName(EkoLabTheme.BUTTON_MULTILINE);
        lab1ExperimentButton.addStyleName(EkoLabTheme.BUTTON_VARIANT_CHOOSER);
        lab1ExperimentButton.addClickListener(event -> {navigator.navigateTo(Lab1ExperimentView.NAME); lab1VariantChooser.close();});

        lab1VariantChooser.setModal(true);
        lab1VariantChooser.setWidth(500.0F, Unit.PIXELS);
        lab1VariantChooser.setHeight(300.0F, Unit.PIXELS);
        lab1VariantChooser.setCaption(i18N.get("lab1.choose.title"));
        lab1VariantChooserContent.setSizeFull();
        lab1VariantChooserContent.setRowExpandRatio(0, 10.0F);
        lab1VariantChooserContent.setRowExpandRatio(1, 1.0F);
        lab1VariantChooserContent.addComponent(lab1RandomDataButton, 0, 1, 0, 1);
        lab1VariantChooserContent.addComponent(lab1ExperimentButton, 1, 1, 1, 1);

        lab3Button.setCaption(i18N.get("lab3.title"));
        lab3Button.setStyleName(EkoLabTheme.BUTTON_MULTILINE);
        lab3Button.addStyleName(EkoLabTheme.BUTTON_CHOOSER);
        lab3Button.addClickListener(event -> navigator.navigateTo(Lab3View.NAME));

        lab1TestButton.setCaption(i18N.get("lab1.test.title"));
        lab1TestButton.setStyleName(EkoLabTheme.BUTTON_MULTILINE);
        lab1TestButton.addStyleName(EkoLabTheme.BUTTON_VARIANT_CHOOSER);
        lab1TestButton.addClickListener(event -> {navigator.navigateTo(Lab1TestView.NAME); labTestChooser.close();});

        /*lab2TestButton.setCaption(i18N.get("lab2.test.title"));
        lab2TestButton.setStyleName(EkoLabTheme.BUTTON_MULTILINE);
        lab2TestButton.addStyleName(EkoLabTheme.BUTTON_VARIANT_CHOOSER);
        lab2TestButton.addClickListener(event -> {navigator.navigateTo(Lab2TestView.NAME); labTestChooser.close();});*/

        lab3TestButton.setCaption(i18N.get("lab3.test.title"));
        lab3TestButton.setStyleName(EkoLabTheme.BUTTON_MULTILINE);
        lab3TestButton.addStyleName(EkoLabTheme.BUTTON_VARIANT_CHOOSER);
        lab3TestButton.addClickListener(event -> {navigator.navigateTo(Lab3TestView.NAME); labTestChooser.close();});

        labTestChooser.setModal(true);
        labTestChooser.setWidth(300.0F, Unit.PIXELS);
        labTestChooser.setHeight(200.0F, Unit.PIXELS);
        labTestChooser.setCaption(i18N.get("labchooser.defence.choose.title"));
        labTestChooserContent.setSizeFull();
        labTestChooserContent.addComponent(lab1TestButton, 0, 0);
        labTestChooserContent.addComponent(lab2TestButton, 0, 1);
        labTestChooserContent.addComponent(lab3TestButton, 0, 2);

        labDefenceButton.setCaption(i18N.get("labchooser.defence"));
        labDefenceButton.setStyleName(EkoLabTheme.BUTTON_MULTILINE);
        labDefenceButton.addStyleName(EkoLabTheme.BUTTON_CHOOSER);
        labDefenceButton.addClickListener(event ->  UI.getCurrent().addWindow(labTestChooser));

        labLabel.setValue(i18N.get("labchooser.title"));
        labLabel.setStyleName(EkoLabTheme.LABEL_HUGE);

        content.addStyleName(EkoLabTheme.PANEL_LABCHOOSER);
        content.addComponent(labLabel, 0, 0);
        content.addComponent(logo, 0, 1, 0, 3);
        content.addComponent(lab1Button, 1, 0);
        content.addComponent(lab2Button, 1, 1);
        content.addComponent(lab3Button, 1, 2);
        content.addComponent(labDefenceButton, 1, 3);

        content.setComponentAlignment(labLabel, Alignment.MIDDLE_CENTER);

        addComponent(content);
    }
}
