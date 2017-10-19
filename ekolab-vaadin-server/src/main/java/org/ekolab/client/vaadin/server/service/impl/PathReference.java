package org.ekolab.client.vaadin.server.service.impl;


import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

public class PathReference implements Closeable {
    private final Path path;
    private final FileSystem fileSystem;

    private PathReference(Path path, FileSystem fileSystem) {
        this.path = path;
        this.fileSystem = fileSystem;
    }

    @Override
    public void close() throws IOException {
        if (this.fileSystem != null)
            this.fileSystem.close();
    }

    public Path getPath() {
        return this.path;
    }

    public FileSystem getFileSystem() {
        return this.fileSystem;
    }

    /**
     * Сначала пробует получить путь в существующей ФС, затем
     * производится попытка найти ресурс в виртуаьной ФС (JAR, WAR и т. д.)
     * @param resPath путь
     * @return экземпляр PathReference
     * @throws IOException возможное исключение
     */
    public static PathReference getPath(final URI resPath) throws IOException {
        try {
            return new PathReference(Paths.get(resPath), null);
        } catch (final FileSystemNotFoundException e) {
            final Map<String, ?> env = Collections.emptyMap();
            final FileSystem fs = FileSystems.newFileSystem(resPath, env);
            return new PathReference(fs.provider().getPath(resPath), fs);
        }
    }
}