package org.ekolab.client.vaadin.server.service;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис, необходмый для получения ресурсов. Кэшируемый.
 */
@Service
public class ResourceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceService.class);

    @Cacheable(cacheNames = "THEME_RESOURCE_IMAGE", key = "#imageName.concat(T(com.vaadin.ui.UI).getCurrent().getTheme())")
    public Image getImage(String imageName) {
        Resource resource = new ThemeResource("img/" + imageName);
        return new Image(null, resource);
    }

    @Cacheable(cacheNames = "THEME_GALLERY_IMAGE_SVG", key = "T(java.util.Objects).hash(#imageName, (T(com.vaadin.ui.UI).getCurrent().getTheme()))")
    public com.github.lotsabackscatter.blueimp.gallery.Image getGalleryImage(String imageName) {
        return loadGalleryImage(getThemeDir() + imageName);
    }

    @Cacheable(cacheNames = "THEME_GALLERY_IMAGE_SVG", key = "T(java.util.Objects).hash(#imagesPath, (T(com.vaadin.ui.UI).getCurrent().getTheme()))")
    public List<com.github.lotsabackscatter.blueimp.gallery.Image> getGalleryImages(String imagesPath) {
        return VaadinServlet.getCurrent().getServletContext().getResourcePaths(getThemeDir() + imagesPath)
                .stream().sorted().map(this::loadGalleryImage).collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "INFO_COMPONENT_HTML", key = "#path.concat(#resourceName).concat(T(com.vaadin.ui.UI).getCurrent().getTheme())")
    public BrowserFrame getHtmlData(String path, String resourceName) {
        return new BrowserFrame(null, new ThemeResource(path + resourceName));
    }

    @Cacheable(cacheNames = "RESOURCE_EXIST", key = "#path.concat(#resourceName).concat(T(com.vaadin.ui.UI).getCurrent().getTheme())")
    public boolean isResourceExists(String path, String resourceName) {
        try {
            return VaadinServlet.getCurrent().getServletContext().
                    getResource(getThemeDir() + path + resourceName) != null;
        } catch (MalformedURLException e) {
            throw  new IllegalArgumentException(e);
        }
    }

    private com.github.lotsabackscatter.blueimp.gallery.Image loadGalleryImage(String path) {
        return new com.github.lotsabackscatter.blueimp.gallery.Image.Builder().
                href(path).
                type(URLConnection.guessContentTypeFromName(path)).build();
    }

    private String getThemeDir() {
        String deploymentServerResourcesPath = VaadinService.getCurrent().getDeploymentConfiguration().getResourcesPath();
        if (StringUtils.hasText(deploymentServerResourcesPath)) {
            LOGGER.info("deploymentServerResourcesPath — " + deploymentServerResourcesPath);
        }
        return (deploymentServerResourcesPath == null ? "/" : deploymentServerResourcesPath + '/') +
                VaadinServlet.THEME_DIR_PATH + '/' + UI.getCurrent().getTheme() + "/";
    }
}
