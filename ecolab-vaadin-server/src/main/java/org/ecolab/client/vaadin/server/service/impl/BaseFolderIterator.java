package org.ecolab.client.vaadin.server.service.impl;

import org.ecolab.client.vaadin.server.service.api.FolderIterator;

import javax.validation.constraints.NotNull;
import java.util.Iterator;

public abstract class BaseFolderIterator implements FolderIterator {
    @Override
    @NotNull
    public Iterator<String> iterator() {
        return this;
    }

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);
}
