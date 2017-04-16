package org.ekolab.client.vaadin.server.service;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис, необходмый для получения ресурсов. Кэшируемый.
 */
@Service
public class ResourceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceService.class.getName());

    @Cacheable(cacheNames = "THEME_RESOURCE_IMAGE", key = "#imageName.concat(T(com.vaadin.ui.UI).getCurrent().getTheme())")
    public Image getImage(String imageName) {
        Resource resource = new ThemeResource("img/" + imageName);
        return new Image(null, resource);
    }

    @Cacheable(cacheNames = "THEME_GALLERY_IMAGE_SVG", key = "T(java.util.Objects).hash(#imageName, (T(com.vaadin.ui.UI).getCurrent().getTheme()))")
    public com.github.lotsabackscatter.blueimp.gallery.Image getGalleryImage(String imageName) {
        String deploymentServerResourcesPath = VaadinService.getCurrent().getDeploymentConfiguration().getResourcesPath();
        if (StringUtils.hasText(deploymentServerResourcesPath)) {
            LOGGER.info("deploymentServerResourcesPath — " + deploymentServerResourcesPath);
        }
        return new com.github.lotsabackscatter.blueimp.gallery.Image.Builder().
                href((deploymentServerResourcesPath == null ? "" : deploymentServerResourcesPath) +
                        VaadinServlet.THEME_DIR_PATH + '/' + UI.getCurrent().getTheme() + "/img/" + imageName).
                type(URLConnection.guessContentTypeFromName(imageName)).build();
    }

    @Cacheable(cacheNames = "THEME_GALLERY_IMAGE_SVG", key = "T(java.util.Objects).hash(#imageNames, (T(com.vaadin.ui.UI).getCurrent().getTheme()))" )
    public List<com.github.lotsabackscatter.blueimp.gallery.Image> getGalleryImages(List<String> imageNames) {
        List<com.github.lotsabackscatter.blueimp.gallery.Image> images = new ArrayList<>(imageNames.size());
        for (String imagePath : imageNames) {
            images.add(getGalleryImage(imagePath));
        }
        return images;
    }
}
