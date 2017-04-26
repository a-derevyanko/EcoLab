package org.ekolab.client.vaadin.server.ui.view.content.lab_3;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.CompositeErrorMessage;
import com.vaadin.server.ErrorMessage;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.service.ResourceService;
import org.ekolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringComponent
@ViewScope
public class Step5 extends GridLayout implements LabWizardStep {
    // ----------------------------- Графические компоненты --------------------------------
    private final Button so2MapButton = new Button("SO2 map", VaadinIcons.MAP_MARKER);
    private final Button ashMapButton = new Button("Ash map", VaadinIcons.MAP_MARKER);
    private final Button zoomInButton = new Button(VaadinIcons.PLUS_CIRCLE_O);
    private final Button zoomOutButton = new Button(VaadinIcons.MINUS_CIRCLE_O);

    @Autowired
    private I18N i18N;

    @Autowired
    private ResourceService resourceService;

    @Override
    public void init() throws IOException {
        LabWizardStep.super.init();
        setSizeFull();
        setMargin(true);
        setRows(20);
        setColumns(20);
        so2MapButton.setSizeFull();
        ashMapButton.setSizeFull();

        so2MapButton.addStyleName(EkoLabTheme.BUTTON_PRIMARY);
        ashMapButton.addStyleName(EkoLabTheme.BUTTON_PRIMARY);
        zoomInButton.addStyleName(EkoLabTheme.BUTTON_PRIMARY);
        zoomOutButton.addStyleName(EkoLabTheme.BUTTON_PRIMARY);

        //addComponent(createBasicDemo(), 4, 4, 19, 9);
    }

    @Override
    public void placeAdditionalComponents(HorizontalLayout buttonsLayout) {
        buttonsLayout.addComponents(zoomInButton, zoomOutButton, so2MapButton, ashMapButton);
    }

    @Override
    public ErrorMessage getComponentError() {
        Set<ErrorMessage> messageList = Stream.of(super.getComponentError())
                .filter(Objects::nonNull).collect(Collectors.toSet());
        return messageList.isEmpty() ? null : new CompositeErrorMessage(messageList);
    }
}
