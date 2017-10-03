package org.ekolab.client.vaadin.server.service.api;

import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.Image;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Сервис, необходмый для получения ресурсов. Кэшируемый.
 */
public interface ResourceService extends Serializable {

    Image getImage(String imageName);

    Image getImage(String path, String imageName);

    com.github.lotsabackscatter.blueimp.gallery.Image getGalleryImage(String imageName);

    List<com.github.lotsabackscatter.blueimp.gallery.Image> getGalleryImages(String imagesPath);

    Set<String> getResourcePaths(String directoryPath);

    BrowserFrame getHtmlData(String path, String resourceName);

    boolean isResourceExists(String path, String resourceName);

    byte[] getZipFolder(FolderSelector folderSelector);

    interface FolderSelector extends Iterable<String> {
        String getFolderName();
    }
}
