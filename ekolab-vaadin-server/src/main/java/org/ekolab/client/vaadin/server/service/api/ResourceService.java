package org.ekolab.client.vaadin.server.service.api;

import com.vaadin.server.Resource;
import com.vaadin.ui.BrowserFrame;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Сервис, необходмый для получения ресурсов. Кэшируемый.
 */
public interface ResourceService extends Serializable {

    Resource getImage(String imageName);

    Resource getImage(String path, String imageName);

    com.github.lotsabackscatter.blueimp.gallery.Image getGalleryImage(String imageName);

    List<com.github.lotsabackscatter.blueimp.gallery.Image> getGalleryImages(String imagesPath);

    Set<String> getResourcePaths(String directoryPath);

    BrowserFrame getHtmlData(String path, String resourceName);

    boolean isResourceExists(String path, String resourceName);

    byte[] getZipFolder(FolderIterator folderIterator);
}
