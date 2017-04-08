package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Image;

/**
 * Created by Андрей on 02.04.2017.
 */
public class ResourceService {
    public static Image getImage(String imageName) {
        Resource resource = new ThemeResource("img/" + imageName);

        return new Image(null, resource);
    }
}
