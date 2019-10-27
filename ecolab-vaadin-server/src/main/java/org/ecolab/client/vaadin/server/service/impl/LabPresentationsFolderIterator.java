package org.ecolab.client.vaadin.server.service.impl;

import org.ecolab.client.vaadin.server.service.api.ResourceService;

import javax.validation.constraints.NotNull;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Селектор, позволяющий выбрать файлы с презентациями лабораторных
 */
public class LabPresentationsFolderIterator extends BaseFolderIterator {
    private int i = 0;
    private Boolean hasNext;

    @NotNull
    private final ResourceService resourceService;

    @NotNull
    private final I18N i18N;

    public LabPresentationsFolderIterator(ResourceService resourceService, I18N i18N) {
        this.resourceService = resourceService;
        this.i18N = i18N;
    }

    @Override
    public String getFolderName() {
        return i18N.get("lab.prefix" , i);
    }

    @Override
    public boolean hasNext() {
        if (hasNext == null) {
            hasNext = resourceService.isResourceExists("content/lab" + (i + 1) + "/presentation", "");
        }
        return hasNext;
    }

    @Override
    public String next() {
        if (hasNext()) {
            i++;
            var next = "content/lab" + i + "/presentation";
            hasNext = null;
            return next;
        } else {
            throw new NoSuchElementException("No element for index " + i);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LabPresentationsFolderIterator)) return false;
        var selector = (LabPresentationsFolderIterator) o;
        return Objects.equals(resourceService, selector.resourceService) &&
                Objects.equals(i18N, selector.i18N);
    }

    @Override
    public int hashCode() {
        return Objects.hash(resourceService, i18N);
    }
}
