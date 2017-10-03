package org.ekolab.client.vaadin.server.service.impl;

import org.ekolab.client.vaadin.server.service.api.ResourceService;

import java.util.NoSuchElementException;

/**
 * Селектор, позволяющий выбрать файлы с презентациями лабораторных
 */
public class LabPresentationsFolderSelector extends BaseFolderSelector {
    private int i = 0;

    private final ResourceService resourceService;

    private final I18N i18N;

    public LabPresentationsFolderSelector(ResourceService resourceService, I18N i18N) {
        this.resourceService = resourceService;
        this.i18N = i18N;
    }

    @Override
    public String getFolderName() {
        return i18N.get("lab.prefix" , i);
    }

    @Override
    public boolean hasNext() {
        return resourceService.isResourceExists("content/lab" + i + "/presentation", "");
    }

    @Override
    public String next() {
        if (hasNext()) {
            i++;
            return "content/lab" + i + "/presentation";
        } else {
            throw new NoSuchElementException();
        }
    }
}
