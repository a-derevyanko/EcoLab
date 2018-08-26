package org.ecolab.client.vaadin.server.ui.common;

import com.vaadin.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.util.function.Supplier;

public class DownloadStreamResource extends StreamResource {
    private static final long CACHE_TIME = 5000L;

    /**
     * Creates a new stream resource for downloading from stream.
     *
     * @param byteArraySource коллбек, через который есть возможность получить загружаемый массив байт.
     * @param filename имя файла
     */
    public DownloadStreamResource(Supplier<byte[]> byteArraySource, String filename) {
        super((StreamSource) () -> new ByteArrayInputStream(byteArraySource.get()), filename);
        setCacheTime(CACHE_TIME);
    }
}
