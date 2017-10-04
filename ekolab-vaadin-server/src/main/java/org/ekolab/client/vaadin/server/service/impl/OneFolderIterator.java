package org.ekolab.client.vaadin.server.service.impl;

import javax.validation.constraints.NotNull;
import java.util.NoSuchElementException;
import java.util.Objects;

public class OneFolderIterator extends BaseFolderIterator {
    private boolean hasNext = true;

    @NotNull
    private final String path;

    @NotNull
    private final String folderName;

    public OneFolderIterator(String folder) {
        this(folder, "");
    }

    public OneFolderIterator(String folder, String folderName) {
        this.path = folder;
        this.folderName = folderName;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public String next() {
        if (hasNext) {
            hasNext = false;
            return path;
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public String getFolderName() {
        return folderName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OneFolderIterator)) return false;
        OneFolderIterator strings = (OneFolderIterator) o;
        return Objects.equals(path, strings.path) &&
                Objects.equals(folderName, strings.folderName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, folderName);
    }
}
