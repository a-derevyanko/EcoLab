package org.ekolab.client.vaadin.server.service.api;

import java.util.Iterator;

public interface FolderIterator extends Iterator<String>, Iterable<String> {
    String getFolderName();
}
