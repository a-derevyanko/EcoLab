package org.ekolab.client.vaadin.server.ui.common;

import com.github.lotsabackscatter.blueimp.gallery.Gallery;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.service.api.PresentationService;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * Created by 777Al on 03.04.2017.
 */
public abstract class LabPresentationStep extends VerticalLayout implements LabWizardStep {
    @Autowired
    protected I18N i18N;

    @Autowired
    protected PresentationService presentationService;

    // ---------------------------- Графические компоненты --------------------
    private final Gallery gallery = new Gallery();
    private final Button showGallery = new Button("Show presentation", VaadinIcons.PRESENTATION);

    @Override
    @PostConstruct
    public void init() throws IOException {
        LabWizardStep.super.init();
        setSizeFull();
        addStyleName(EkoLabTheme.PANEL_WIZARD_PRESENTATION);
        addComponent(gallery);
        showGallery.setCaption(i18N.get("lab.presentation.show-presentation"));
        showGallery.addStyleName(EkoLabTheme.BUTTON_PRIMARY);
        showGallery.addStyleName(EkoLabTheme.BUTTON_TINY);
        showGallery.addClickListener(event -> gallery.showGallery(presentationService.getPresentationSlides(getLabNumber()), presentationService.getPresentationOptions()));
    }

    @NotEmpty
    @NotNull
    protected abstract int getLabNumber();

    @Override
    public boolean onBack() {
        return false;
    }

    @Override
    public void placeAdditionalComponents(HorizontalLayout buttonsLayout) {
        buttonsLayout.addComponent(showGallery);
        buttonsLayout.setComponentAlignment(showGallery, Alignment.MIDDLE_CENTER);
    }
}
