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
    private final GridLayout content = new GridLayout(10, 10);
    private final NativeButton lab1Button = new NativeButton("Laboratory work №1");
    private final NativeButton lab2Button = new NativeButton("Laboratory work №2");
    private final NativeButton lab3Button = new NativeButton("Laboratory work №3");
    private final NativeButton labDefenceButton = new NativeButton("Defence of laboratory works");
    private final Label  labLabel = new Label("Environmental technologies at TPPs");

    @Override
    public void init() {
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

        lab1Button.setSizeFull();
        lab1Button.setStyleName(EkoLabTheme.BUTTON_HUGE);
        lab1Button.setStyleName(EkoLabTheme.BUTTON_PRIMARY);

        lab2Button.setSizeFull();
        lab2Button.setStyleName(EkoLabTheme.BUTTON_HUGE);
        lab2Button.setStyleName(EkoLabTheme.BUTTON_PRIMARY);

        lab3Button.setSizeFull();
        lab3Button.setCaption(i18N.get("lab3.title"));
        lab3Button.setStyleName(EkoLabTheme.BUTTON_MULTILINE);
        lab3Button.addStyleName(EkoLabTheme.BUTTON_HUGE);
        lab3Button.addClickListener(event -> navigator.navigateTo(Lab3View.NAME));

        labDefenceButton.setSizeFull();
        labDefenceButton.setStyleName(EkoLabTheme.BUTTON_HUGE);
        labDefenceButton.setStyleName(EkoLabTheme.BUTTON_PRIMARY);

        labLabel.setStyleName(EkoLabTheme.LABEL_HUGE);

        content.addStyleName(EkoLabTheme.PANEL_LABCHOOSER);
        content.addComponent(labLabel, 0, 1, 2, 3);
        content.addComponent(logo, 0, 4, 2, 8);
        content.addComponent(lab1Button, 3, 1, 9, 2);
        content.addComponent(lab2Button, 3, 3, 9, 4);
        content.addComponent(lab3Button, 3, 5, 9, 6);
        content.addComponent(labDefenceButton, 3, 7, 9, 8);

        content.setComponentAlignment(labLabel, Alignment.MIDDLE_CENTER);

        addComponent(content);
    }
}
