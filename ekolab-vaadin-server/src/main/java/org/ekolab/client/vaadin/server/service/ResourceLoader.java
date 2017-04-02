package org.ekolab.client.vaadin.server.service;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Image;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by Андрей on 02.04.2017.
 */
@Service
public class ResourceLoader {
    private static final String CACHE_NAME = "RESOURCE_LOADER";

    @Cacheable(CACHE_NAME)
    public Image getImage(String imageName) {
        Resource resource = new ThemeResource("img/" + imageName);

        return new Image(null, resource);
    }
}
