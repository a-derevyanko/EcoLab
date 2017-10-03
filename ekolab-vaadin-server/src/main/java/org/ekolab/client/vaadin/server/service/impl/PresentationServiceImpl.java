package org.ekolab.client.vaadin.server.service.impl;

import com.github.lotsabackscatter.blueimp.gallery.Image;
import com.github.lotsabackscatter.blueimp.gallery.Options;
import com.vaadin.spring.annotation.UIScope;
import org.ekolab.client.vaadin.server.service.api.PresentationService;
import org.ekolab.client.vaadin.server.service.api.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@UIScope
public class PresentationServiceImpl implements PresentationService {
    @Autowired
    private final ResourceService resourceService;

    public PresentationServiceImpl(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @Override
    @Cacheable("PRESENTATION_SLIDES")
    public List<Image> getPresentationSlides(int labNumber) {
        return resourceService.getGalleryImages( "content/lab" + labNumber + "/presentation");
    }

    @Override
    @Cacheable("PRESENTATION_OPTIONS")
    public Options getPresentationOptions() {
        Options options = new Options();
        options.unloadElements = false;
        options.preloadRange = 2;
        options.closeOnSlideClick = false;
        options.closeOnSwipeUpOrDown = false;
        options.carousel = false;
        return options;
    }
}
