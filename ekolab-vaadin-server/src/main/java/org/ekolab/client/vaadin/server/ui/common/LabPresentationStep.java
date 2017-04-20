package org.ekolab.client.vaadin.server.ui.common;

import com.github.lotsabackscatter.blueimp.gallery.Gallery;
import com.github.lotsabackscatter.blueimp.gallery.Image;
import com.github.lotsabackscatter.blueimp.gallery.Options;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.service.ResourceService;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by 777Al on 03.04.2017.
 */
public abstract class LabPresentationStep extends VerticalLayout implements LabWizardStep {
    @Autowired
    protected I18N i18N;

    @Autowired
    protected ResourceService resourceService;

    // ---------------------------- Графические компоненты --------------------
    private final Gallery gallery = new Gallery();
    private final Button showGallery = new Button("Show presentation", VaadinIcons.PRESENTATION);

    @Override
    @PostConstruct
    public void init() {
        LabWizardStep.super.init();
        setSizeFull();
        addComponent(gallery);
        addComponent(showGallery);
        setComponentAlignment(showGallery, Alignment.MIDDLE_CENTER);
        showGallery.setCaption(i18N.get("lab.presentation.show-presentation"));
        showGallery.setStyleName(EkoLabTheme.BUTTON_HUGE);
        showGallery.setStyleName(EkoLabTheme.BUTTON_PRIMARY);
        showGallery.addClickListener(event -> gallery.showGallery(getPresentationSlides(), getPresentationOptions()));
    }

    protected Options getPresentationOptions() {
        Options options = new Options();
        options.unloadElements = false;
        options.preloadRange = 2;
        options.closeOnSlideClick = false;
        options.closeOnSwipeUpOrDown = false;
        options.carousel = false;
        return options;
    }

    @NotEmpty
    @NotNull
    protected List<Image> getPresentationSlides() {
        return resourceService.getGalleryImages(getLabContentFolder());
    }

    @NotEmpty
    @NotNull
    protected abstract String getLabContentFolder();

    @Override
    public boolean onBack() {
        return false;
    }
}
