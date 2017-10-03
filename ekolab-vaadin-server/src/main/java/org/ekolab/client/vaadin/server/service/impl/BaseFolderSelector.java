package org.ekolab.client.vaadin.server.service.impl;

import org.ekolab.client.vaadin.server.service.api.ResourceService;

import javax.validation.constraints.NotNull;
import java.util.Iterator;

public abstract class BaseFolderSelector implements Iterator<String>, ResourceService.FolderSelector {
    @Override
    @NotNull
    public Iterator<String> iterator() {
        return this;
    }
}
