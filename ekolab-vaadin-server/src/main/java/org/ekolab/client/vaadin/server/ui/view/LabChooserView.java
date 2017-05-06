package org.ekolab.client.vaadin.server.ui.view;

import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.ui.EkoLabNavigator;
import org.ekolab.client.vaadin.server.service.ResourceService;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.client.vaadin.server.ui.view.api.View;
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
    private final Label  labLabel = new Label("Environmental technologies at TPPs");

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

        lab1Button.setStyleName(EkoLabTheme.BUTTON_MULTILINE);
        lab1Button.addStyleName(EkoLabTheme.BUTTON_CHOOSER);

        lab2Button.setStyleName(EkoLabTheme.BUTTON_MULTILINE);
        lab2Button.addStyleName(EkoLabTheme.BUTTON_CHOOSER);

        lab3Button.setCaption(i18N.get("lab3.title"));
        lab3Button.setStyleName(EkoLabTheme.BUTTON_MULTILINE);
        lab3Button.addStyleName(EkoLabTheme.BUTTON_CHOOSER);
        lab3Button.addClickListener(event -> navigator.navigateTo(Lab3View.NAME));

        labDefenceButton.setStyleName(EkoLabTheme.BUTTON_MULTILINE);
        labDefenceButton.addStyleName(EkoLabTheme.BUTTON_CHOOSER);;

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
