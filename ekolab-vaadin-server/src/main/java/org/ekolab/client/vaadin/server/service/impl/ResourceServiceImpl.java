package org.ekolab.client.vaadin.server.service.impl;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.BrowserFrame;
import com.vaadin.ui.UI;
import org.ekolab.client.vaadin.server.service.api.FolderIterator;
import org.ekolab.client.vaadin.server.service.api.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.vaadin.spring.context.VaadinApplicationContext;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
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

    @Autowired
    private final I18N i18N;

    public ResourceServiceImpl(I18N i18N) {
        this.i18N = i18N;
    }

    @Override
    public Resource getImage(String imageName) {
        return getImage("img/" , imageName);
    }

    @Override
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
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (ZipOutputStream zos = new ZipOutputStream(bos)) {
                for (String path : folderIterator) {
                    try (Stream<Path> paths = Files.walk(getFile(path).toPath())) {
                        paths.filter(p -> Files.isRegularFile(p))
                                .forEach(p -> addFileToZip(folderIterator.getFolderName(),
                                        p.toFile(), zos, false));
                    }
                }
            }

            return bos.toByteArray();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
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

    /*
     * Рекурсивно добавляет файлы в архив
     */
    private void addFileToZip(String path, File srcFile, ZipOutputStream zip, boolean emptyFolder) {
        try {
            if (emptyFolder) {
                zip.putNextEntry(new ZipEntry(Paths.get(path,  srcFile.getName(), "").toString()));
            } else {
                if (srcFile.isDirectory()) {
                    addFolderToZip(path, srcFile, zip);
                } else {
                    zip.putNextEntry(new ZipEntry(Paths.get(path, srcFile.getName()).toString()));
                    Files.copy(srcFile.toPath(), zip);
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
    private void addFolderToZip(String path, File folder, ZipOutputStream zip) {
        try {
            File[] files = folder.listFiles();

            if (files == null) {
                throw new IllegalArgumentException("Empty folder: " + folder.getCanonicalPath());
            }

            if (files.length == 0) {
                addFileToZip(path, folder, zip, true);
            } else {
                for (File file : files) {
                    addFileToZip(path.isEmpty() ? folder.getName() : path + "/" + folder.getName(),
                            file, zip, false);
                }
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private File getFile(String path) {
        try {
            return VaadinApplicationContext.getContext().getResource(getThemeDir() + path).getFile();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
