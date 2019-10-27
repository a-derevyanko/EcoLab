package org.ecolab.client.vaadin.server.service.impl;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.UI;
import org.apache.commons.lang.UnhandledException;
import org.ecolab.client.vaadin.server.service.api.FolderIterator;
import org.ecolab.client.vaadin.server.service.api.ResourceService;
import org.ecolab.server.common.PathReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Сервис, необходмый для получения ресурсов. Кэшируемый.
 */
@Service
@UIScope
public class ResourceServiceImpl implements ResourceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceServiceImpl.class);

    private final I18N i18N;

    public ResourceServiceImpl(I18N i18N) {
        this.i18N = i18N;
    }

    @Override
    @Cacheable("RESOURCE")
    public Resource getImage(String imageName) {
        return getImage("img/" , imageName);
    }

    @Override
    @Cacheable("RESOURCE")
    public Resource getImage(String path, String imageName) {
        return new ThemeResource(path + imageName);
    }

    @Override
    public com.github.lotsabackscatter.blueimp.gallery.Image getGalleryImage(String imageName) {
        return loadGalleryImage(getThemeDir() + imageName);
    }

    @Override
    public List<com.github.lotsabackscatter.blueimp.gallery.Image> getGalleryImages(String imagesPath) {
        return VaadinServlet.getCurrent().getServletContext().getResourcePaths(getThemeDir() + imagesPath)
                .stream().sorted().map(this::loadGalleryImage).collect(Collectors.toList());
    }

    @Override
    public Set<String> getResourcePaths(String directoryPath) {
        return VaadinServlet.getCurrent().getServletContext().getResourcePaths(getThemeDir() + directoryPath);
    }

    @Override
    public BrowserFrame getHtmlData(String path, String resourceName) {
        return new BrowserFrame(null, new ThemeResource(path + resourceName));
    }

    @Override
    @Cacheable(cacheNames = "RESOURCE_EXIST", key = "#path.concat(#resourceName).concat(T(com.vaadin.ui.UI).getCurrent().getTheme())")
    public boolean isResourceExists(String path, String resourceName) {
        try {
            return VaadinServlet.getCurrent().getServletContext().
                    getResource(getThemeDir() + path + resourceName) != null;
        } catch (MalformedURLException e) {
            throw  new IllegalArgumentException(e);
        }
    }

    /**
     * Собирает архив с файлами
     * Архивы больше 100 мб не кэшируются
     * @param folderIterator селектор для папок, айлы из которых будут добавлены в архив
     * @return zip - архив
     */
    @Override
    @Cacheable(value = "FILES_ARCHIVE", unless = "#result.length > 104857600")
    public byte[] getZipFolder(FolderIterator folderIterator) {
        try (var bos = new ByteArrayOutputStream()) {
            try (var zos = new ZipOutputStream(bos)) {
                for (var path : folderIterator) {
                    try (var pathReference = PathReference.getPath(VaadinServlet.getCurrent().getServletContext().getResource(getThemeDir() + path).toURI());
                         var pathStream = Files.list(pathReference.getPath())) {
                        pathStream.filter(Files::isRegularFile).
                                forEach(p -> addFileToZip(folderIterator.getFolderName(), p, zos, false));
                    }
                }
            }

            return bos.toByteArray();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        } catch (URISyntaxException e) {
            throw new UnhandledException(e);
        }
    }

    private com.github.lotsabackscatter.blueimp.gallery.Image loadGalleryImage(String path) {
        return new com.github.lotsabackscatter.blueimp.gallery.Image.Builder().
                href(path).
                type(URLConnection.guessContentTypeFromName(path)).build();
    }

    private String getThemeDir() {
        var deploymentServerResourcesPath = VaadinService.getCurrent().getDeploymentConfiguration().getResourcesPath();
        if (StringUtils.hasText(deploymentServerResourcesPath)) {
            LOGGER.info("deploymentServerResourcesPath — " + deploymentServerResourcesPath);
        }
        return (deploymentServerResourcesPath == null ? "/" : deploymentServerResourcesPath + '/') +
                VaadinServlet.THEME_DIR_PATH + '/' + UI.getCurrent().getTheme() + "/";
    }

    /*
     * Рекурсивно добавляет файлы в архив
     */
    private void addFileToZip(String path, Path srcFile, ZipOutputStream zip, boolean emptyFolder) {
        try {
            if (emptyFolder) {
                zip.putNextEntry(new ZipEntry(Paths.get(path,  srcFile.getFileName().toString(), "").toString()));
            } else {
                if (Files.isDirectory(srcFile)) {
                    addFolderToZip(path, srcFile, zip);
                } else {
                    zip.putNextEntry(new ZipEntry(Paths.get(path, srcFile.getFileName().toString()).toString()));
                    Files.copy(srcFile, zip);
                    zip.closeEntry();
                }
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    /**
     * Добавляет папку в ZIP файл
     * @param path путь
     * @param folder папка
     * @param zip архив
     */
    private void addFolderToZip(String path, Path folder, ZipOutputStream zip) {
        try {
            var files = Files.list(folder).collect(Collectors.toList());

            if (files.isEmpty()) {
                addFileToZip(path, folder, zip, true);
            } else {
                for (var file : files) {
                    addFileToZip(path.isEmpty() ? folder.getFileName().toString() : path + "/" + folder.getFileName().toString(),
                            file, zip, false);
                }
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
