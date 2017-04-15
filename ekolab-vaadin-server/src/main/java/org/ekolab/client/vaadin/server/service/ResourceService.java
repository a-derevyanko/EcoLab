package org.ekolab.client.vaadin.server.service;

import com.google.common.net.MediaType;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис, необходмый для получения ресурсов. Кэшируемый.
 */
@Service
public class ResourceService {
    @Cacheable(cacheNames = "THEME_RESOURCE_IMAGE", key = "#imageName.concat(T(com.vaadin.ui.UI).getCurrent().getTheme())")
    public Image getImage(String imageName) {
        Resource resource = new ThemeResource("img/" + imageName);
        return new Image(null, resource);
    }

    @Cacheable(cacheNames = "THEME_IMAGE_SVG", key = "#imagePath.concat(T(com.vaadin.ui.UI).getCurrent().getTheme())" )
    public com.github.lotsabackscatter.blueimp.gallery.Image svg(String imagePath) {
        String deploymentServerResourcesPath = VaadinService.getCurrent().getDeploymentConfiguration().getResourcesPath();
        return new com.github.lotsabackscatter.blueimp.gallery.Image.Builder().
                href((deploymentServerResourcesPath == null ? "" : deploymentServerResourcesPath) +
                "/VAADIN/themes/" + UI.getCurrent().getTheme() + "/" + imagePath).
                type(MediaType.SVG_UTF_8.toString()).build();
    }

    @Cacheable(cacheNames = "THEME_IMAGE_SVG", key = "T(java.util.Arrays).toString(#imagePaths).concat(T(com.vaadin.ui.UI).getCurrent().getTheme())" )
    public List<com.github.lotsabackscatter.blueimp.gallery.Image> svg(String... imagePaths) {
        List<com.github.lotsabackscatter.blueimp.gallery.Image> images = new ArrayList<>(imagePaths.length);
        for (String imagePath : imagePaths) {
            images.add(svg(imagePath));
        }
        return images;
    }
}
