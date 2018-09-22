package org.ecolab.client.vaadin.server.ui.view;

import com.github.lotsabackscatter.blueimp.gallery.Gallery;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.ecolab.client.vaadin.server.service.api.PresentationService;
import org.ecolab.client.vaadin.server.service.api.ResourceService;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.EcoLabNavigator;
import org.ecolab.client.vaadin.server.ui.VaadinUI;
import org.ecolab.client.vaadin.server.ui.styles.EcoLabTheme;
import org.ecolab.client.vaadin.server.ui.view.api.View;
import org.ecolab.client.vaadin.server.ui.view.content.lab_1.Lab1TestView;
import org.ecolab.client.vaadin.server.ui.view.content.lab_1.experiment.Lab1ExperimentView;
import org.ecolab.client.vaadin.server.ui.view.content.lab_1.random.Lab1RandomDataView;
import org.ecolab.client.vaadin.server.ui.view.content.lab_2.Lab2TestView;
import org.ecolab.client.vaadin.server.ui.view.content.lab_2.experiment.Lab2ExperimentView;
import org.ecolab.client.vaadin.server.ui.view.content.lab_2.random.Lab2RandomDataView;
import org.ecolab.client.vaadin.server.ui.view.content.lab_3.Lab3TestView;
import org.ecolab.client.vaadin.server.ui.view.content.lab_3.Lab3View;
import org.ecolab.client.vaadin.server.ui.windows.LabTypeSelectorWindow;
import org.ecolab.server.common.CurrentUser;
import org.ecolab.server.model.LabMode;
import org.ecolab.server.model.UserGroup;
import org.ecolab.server.service.api.StudentInfoService;
import org.ecolab.server.service.api.content.UserLabService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by 777Al on 03.04.2017.
 */
@SpringView(name = LabChooserView.NAME)
public class LabChooserView extends VerticalLayout implements View {
    public static final String NAME = "labchooser";

    private final EcoLabNavigator navigator;

    private final ResourceService resourceService;

    private final UserLabService userLabService;

    private final StudentInfoService studentInfoService;

    private final PresentationService presentationService;

    private final I18N i18N;

    private final LabTypeSelectorWindow labTypeSelectorWindow;

    // ---------------------------- Графические компоненты --------------------
    private final Gallery gallery = new Gallery();
    private final GridLayout content = new GridLayout(2, 5);
    private final Image logo = new Image();
    private final NativeButton lab1Button = new NativeButton("Laboratory work №1");
    private final NativeButton lab2Button = new NativeButton("Laboratory work №2");
    private final NativeButton lab3Button = new NativeButton("Laboratory work №3");
    private final NativeButton labDefenceButton = new NativeButton("Defence of laboratory works");
    private final Button labContentButton = new Button("Content of laboratory works");
    private final NativeButton lab1TestButton = new NativeButton("Lab 1 test");
    private final NativeButton lab2TestButton = new NativeButton("Lab 2 test");
    private final NativeButton lab3TestButton = new NativeButton("Lab 3 test");
    private final Button lab1PresentationButton = new Button(VaadinIcons.PRESENTATION);
    private final Button lab2PresentationButton = new Button(VaadinIcons.PRESENTATION);
    private final Button lab3PresentationButton = new Button(VaadinIcons.PRESENTATION);
    private final Button downloadPresentationButton = new Button(VaadinIcons.DOWNLOAD);
    private final AbsoluteLayout labTestChooserContent = new AbsoluteLayout();
    private final VerticalLayout labTestChooserButtons = new VerticalLayout();
    private final Window labTestChooser = new Window("Choose lab for test", labTestChooserContent);
    private final VerticalLayout labPresentationSelectContent = new VerticalLayout();
    private final PopupView labPresentationSelectView = new PopupView(null, labPresentationSelectContent);

    @Autowired
    public LabChooserView(EcoLabNavigator navigator, ResourceService resourceService, UserLabService userLabService, StudentInfoService studentInfoService, PresentationService presentationService, I18N i18N, LabTypeSelectorWindow labTypeSelectorWindow) {
        this.navigator = navigator;
        this.resourceService = resourceService;
        this.userLabService = userLabService;
        this.studentInfoService = studentInfoService;
        this.presentationService = presentationService;
        this.i18N = i18N;
        this.labTypeSelectorWindow = labTypeSelectorWindow;
    }

    @Override
    public void init() throws Exception {
        View.super.init();
        addStyleName(EcoLabTheme.VIEW_LABCHOOSER);
        setMargin(false);
        setSpacing(false);

        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        content.setSizeFull();
        content.setSpacing(true);

        logo.setSource(resourceService.getImage(EcoLabTheme.IMAGE_LOGO));
        logo.setSizeFull();
        logo.setWidth(400.0F, Unit.PIXELS);

        createLabButton(lab1Button, i18N.get("lab1.title"), event ->  labTypeSelectorWindow.show(
                new LabTypeSelectorWindow.LabTypeSelectorWindowSettings(Lab1RandomDataView.NAME, Lab1ExperimentView.NAME)));

        createLabButton(lab2Button, i18N.get("lab2.title"), event ->  labTypeSelectorWindow.show(
                new LabTypeSelectorWindow.LabTypeSelectorWindowSettings(Lab2RandomDataView.NAME, Lab2ExperimentView.NAME)));

        createLabButton(lab3Button, i18N.get("lab3.title"), event ->  navigator.navigateTo(Lab3View.NAME));

        createLabTestButton(lab1TestButton, i18N.get("lab1.test.title"), event -> {navigator.navigateTo(Lab1TestView.NAME); labTestChooser.close();});

        createLabTestButton(lab2TestButton, i18N.get("lab2.test.title"), event -> {navigator.navigateTo(Lab2TestView.NAME); labTestChooser.close();});

        createLabTestButton(lab3TestButton, i18N.get("lab3.test.title"), event -> {navigator.navigateTo(Lab3TestView.NAME); labTestChooser.close();});

        createLabButton(labDefenceButton, i18N.get("labchooser.defence"), event ->  UI.getCurrent().addWindow(labTestChooser));

        createPresentationButton(lab1PresentationButton, i18N.get("labchooser.lab-content-1"), 1);
        createPresentationButton(lab2PresentationButton, i18N.get("labchooser.lab-content-2"), 2);
        createPresentationButton(lab3PresentationButton, i18N.get("labchooser.lab-content-3"), 3);
        downloadPresentationButton.setCaption(i18N.get("labchooser.lab-content-download"));
        downloadPresentationButton.setHeight(45.0F, Unit.PIXELS);
        downloadPresentationButton.setStyleName(EcoLabTheme.BUTTON_PRIMARY);
        downloadPresentationButton.setSizeFull();

        FileDownloader fileDownloader = new FileDownloader(new StreamResource(
                () -> getClass().getClassLoader().getResourceAsStream("org/ecolab/server/manual.pdf"), "manual.pdf"));

        fileDownloader.extend(downloadPresentationButton);

        labPresentationSelectContent.addComponent(lab1PresentationButton);
        labPresentationSelectContent.addComponent(lab2PresentationButton);
        labPresentationSelectContent.addComponent(lab3PresentationButton);
        labPresentationSelectContent.addComponent(downloadPresentationButton);
        labPresentationSelectView.setHideOnMouseOut(true);

        labTestChooser.setModal(true);
        labTestChooser.setResizable(false);
        labTestChooser.setWidth(950, Unit.PIXELS);
        labTestChooser.setHeight(600, Unit.PIXELS);
        labTestChooser.setCaption(i18N.get("labchooser.defence.choose.title"));
        labTestChooserContent.setSizeFull();
        labTestChooserContent.setStyleName(EcoLabTheme.PANEL_TEST_PRESENTATION);
        labTestChooserContent.addComponent(labTestChooserButtons, EcoLabTheme.toCssPosition(520, 310));
        labTestChooserButtons.setSpacing(true);
        labTestChooserButtons.addComponent(lab1TestButton);
        labTestChooserButtons.addComponent(lab2TestButton);
        labTestChooserButtons.addComponent(lab3TestButton);

        labContentButton.setCaption(i18N.get("labchooser.lab-content"));
        labContentButton.setStyleName(EcoLabTheme.BUTTON_PRIMARY);
        labContentButton.addClickListener(event ->  labPresentationSelectView.setPopupVisible(true));

        content.addStyleName(EcoLabTheme.PANEL_LABCHOOSER);
        content.addComponent(logo, 0, 0, 0, 2);
        content.addComponent(lab1Button, 1, 0);
        content.addComponent(lab2Button, 1, 1);
        content.addComponent(lab3Button, 1, 2);
        content.addComponent(labDefenceButton, 1, 3);
        content.addComponent(labContentButton, 0, 3);
        content.addComponent(labPresentationSelectView, 0, 4);
        content.addComponent(gallery, 1, 4);

        content.setComponentAlignment(labContentButton, Alignment.BOTTOM_CENTER);
        content.setComponentAlignment(lab1Button, Alignment.MIDDLE_RIGHT);
        content.setComponentAlignment(lab2Button, Alignment.MIDDLE_RIGHT);
        content.setComponentAlignment(lab3Button, Alignment.MIDDLE_RIGHT);
        content.setComponentAlignment(labDefenceButton, Alignment.MIDDLE_RIGHT);
        content.setRowExpandRatio(0, 0.2F);
        content.setRowExpandRatio(1, 0.2F);
        content.setRowExpandRatio(2, 0.2F);
        content.setRowExpandRatio(3, 0.1999F);
        content.setRowExpandRatio(4, 0.0001F);

        addComponent(content);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        Set<Integer> allowedLabs = studentInfoService.getAllowedLabs(CurrentUser.getId());
        Set<Integer> allowedTests = studentInfoService.getAllowedDefence(CurrentUser.getId());
        Map<Integer, LabMode> completedLabs = userLabService.getCompletedLabs();
        Collection<Integer> completedTests = userLabService.getCompletedTests();
        boolean isNotStudent = VaadinUI.getCurrent().getCurrentUserInfo().getGroup() != UserGroup.STUDENT;
        setTestButtonSate(lab1TestButton, (isNotStudent || allowedTests.contains(1)) && completedLabs.containsKey(1) && !completedTests.contains(1));
        setTestButtonSate(lab2TestButton, (isNotStudent || allowedTests.contains(2)) && completedLabs.containsKey(2) && !completedTests.contains(2));
        setTestButtonSate(lab3TestButton, (isNotStudent || allowedTests.contains(3)) && completedLabs.containsKey(3) && !completedTests.contains(3));
        setLabButtonSate(lab1Button, isNotStudent || (allowedLabs.contains(1) && !completedLabs.containsKey(1)));
        setLabButtonSate(lab2Button, isNotStudent || (allowedLabs.contains(2) && !completedLabs.containsKey(2)));
        setLabButtonSate(lab3Button, isNotStudent || (allowedLabs.contains(3) && !completedLabs.containsKey(3)));
        setButtonSate(labDefenceButton, !completedLabs.isEmpty());
    }

    private void setButtonSate(NativeButton button, boolean enabled) {
        if (enabled) {
            button.setEnabled(true);
            button.removeStyleName(EcoLabTheme.BUTTON_DISABLED);
        } else {
            button.setEnabled(false);
            button.addStyleName(EcoLabTheme.BUTTON_DISABLED);
        }
    }

    private void setLabButtonSate(NativeButton button, boolean enabled) {
        setButtonSate(button, enabled);
        button.setDescription(enabled ? "" : i18N.get("labchooser.lab-not-completed"));
    }

    private void setTestButtonSate(NativeButton button, boolean enabled) {
        setButtonSate(button, enabled);
        button.setDescription(enabled ? "" : i18N.get("test.lab-not-completed"));
    }

    private void createLabButton(Button button, String title, Button.ClickListener listener) {
        button.setCaption(title);
        button.setStyleName(EcoLabTheme.BUTTON_MULTILINE);
        button.addStyleName(EcoLabTheme.BUTTON_CHOOSER);
        button.addClickListener(listener);
    }

    private void createLabTestButton(Button button, String title, Button.ClickListener listener) {
        button.setCaption(title);
        button.setStyleName(EcoLabTheme.BUTTON_MULTILINE);
        button.addStyleName(EcoLabTheme.BUTTON_TEST_VARIANT_CHOOSER);
        button.addClickListener(listener);
    }

    private void createPresentationButton(Button button, String title, int labNumber) {
        button.setCaption(title);
        button.setHeight(45.0F, Unit.PIXELS);
        button.setStyleName(EcoLabTheme.BUTTON_PRIMARY);
        button.setSizeFull();
        button.addClickListener(event -> gallery.showGallery(presentationService.getPresentationSlides(labNumber), presentationService.getPresentationOptions()));
    }
}
