package org.ekolab.client.vaadin.server.ui.view;

import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.ekolab.client.vaadin.server.ui.EkoLabNavigator;
import org.ekolab.client.vaadin.server.ui.common.ResourceService;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.ekolab.client.vaadin.server.ui.view.api.View;
import org.ekolab.client.vaadin.server.ui.view.content.lab_3.Lab3View;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * Created by 777Al on 03.04.2017.
 */
@SpringView(name = LabChooserView.NAME)
public class LabChooserView extends VerticalLayout implements View {
    public static final String NAME = "labchooser";

    @Autowired
    private EkoLabNavigator navigator;

    // ---------------------------- Графические компоненты --------------------
    private final GridLayout content = new GridLayout(10, 10);
    private final Button lab1Button = new Button("Laboratory work №1");
    private final Button lab2Button = new Button("Laboratory work №2");
    private final Button lab3Button = new Button("Laboratory work №3");
    private final Button labDefenceButton = new Button("Defence of laboratory works");
    private final Label  labLabel = new Label("Environmental technologies at TPPs");

    @Override
    @PostConstruct
    public void init() {
        setSizeFull();
        addStyleName(EkoLabTheme.VIEW_LABCHOOSER);
        setMargin(false);
        setSpacing(false);

        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        content.setSizeFull();
        content.setMargin(true);
        content.setSpacing(true);
        Responsive.makeResponsive(content);

        Image logo = ResourceService.getImage(EkoLabTheme.IMAGE_LOGO);
        logo.setSizeFull();

        lab1Button.setSizeFull();
        lab1Button.setStyleName(EkoLabTheme.BUTTON_HUGE);
        lab1Button.setStyleName(EkoLabTheme.BUTTON_PRIMARY);

        lab2Button.setSizeFull();
        lab2Button.setStyleName(EkoLabTheme.BUTTON_HUGE);
        lab2Button.setStyleName(EkoLabTheme.BUTTON_PRIMARY);

        lab3Button.setSizeFull();
        lab3Button.setStyleName(EkoLabTheme.BUTTON_HUGE);
        lab3Button.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
        lab3Button.addClickListener(event -> navigator.navigateTo(Lab3View.NAME));

        labDefenceButton.setSizeFull();
        labDefenceButton.setStyleName(EkoLabTheme.BUTTON_HUGE);
        labDefenceButton.setStyleName(EkoLabTheme.BUTTON_PRIMARY);

        labLabel.setStyleName(EkoLabTheme.LABEL_HUGE);

        content.addStyleName(EkoLabTheme.PANEL_LABCHOOSER);
        content.addComponent(labLabel, 0, 1, 4, 3);
        content.addComponent(logo, 0, 4, 4, 8);
        content.addComponent(lab1Button, 5, 1, 9, 2);
        content.addComponent(lab2Button, 5, 3, 9, 4);
        content.addComponent(lab3Button, 5, 5, 9, 6);
        content.addComponent(labDefenceButton, 5, 7, 9, 8);

        content.setComponentAlignment(labLabel, Alignment.MIDDLE_CENTER);

        addComponent(content);
    }
}
