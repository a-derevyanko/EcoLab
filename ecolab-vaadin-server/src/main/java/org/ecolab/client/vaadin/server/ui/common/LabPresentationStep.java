package org.ecolab.client.vaadin.server.ui.common;

import com.github.lotsabackscatter.blueimp.gallery.Gallery;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.ecolab.client.vaadin.server.service.api.PresentationService;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.styles.EcoLabTheme;
import org.ecolab.server.model.content.LabData;
import org.ecolab.server.model.content.LabVariant;
import org.hibernate.validator.constraints.NotEmpty;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

/**
 * Created by 777Al on 03.04.2017.
 */
public abstract class LabPresentationStep<T extends LabData<V>, V extends LabVariant> extends VerticalLayout implements org.ecolab.client.vaadin.server.ui.common.LabWizardStep<T, V> {
    protected final I18N i18N;

    protected final PresentationService presentationService;

    // ---------------------------- Графические компоненты --------------------
    private final Gallery gallery = new Gallery();
    private final Button showGallery = new Button("Show presentation", VaadinIcons.PRESENTATION);

    protected LabPresentationStep(I18N i18N, PresentationService presentationService) {
        this.i18N = i18N;
        this.presentationService = presentationService;
    }

    @Override
    @PostConstruct
    public void init() {
        LabWizardStep.super.init();
        setSizeFull();
        addStyleName(getPanelStyleName());
        addComponent(gallery);
        showGallery.setCaption(i18N.get("lab.presentation.show-presentation"));
        showGallery.addStyleName(EcoLabTheme.BUTTON_PRIMARY);
        showGallery.addStyleName(EcoLabTheme.BUTTON_TINY);
        showGallery.addClickListener(event -> gallery.showGallery(presentationService.getPresentationSlides(getLabNumber()), presentationService.getPresentationOptions()));
    }

    @NotEmpty
    @NotNull
    protected abstract int getLabNumber();

    @NotEmpty
    @NotNull
    protected String getPanelStyleName() {
        return EcoLabTheme.PANEL_WIZARD_PRESENTATION;
    }

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
