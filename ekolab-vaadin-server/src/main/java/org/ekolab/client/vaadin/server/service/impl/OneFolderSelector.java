package org.ekolab.client.vaadin.server.service.impl;

import java.util.NoSuchElementException;

public class OneFolderSelector extends BaseFolderSelector {
    private boolean hasNext = true;

    private final String path;

    private final String folderName;

    public OneFolderSelector(String folder) {
        this(folder, "");
    }

    public OneFolderSelector(String folder, String folderName) {
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
}
