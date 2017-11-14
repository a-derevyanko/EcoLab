package org.ekolab.client.vaadin.server.ui.view;

import org.ekolab.client.vaadin.server.service.api.ResourceService;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.windows.ResourceWindow;

public abstract class LabViewUtils {
    public static void showHelpBeforeLab(ResourceWindow resourceWindow, I18N i18N, ResourceService resourceService) {
        resourceWindow.show(new ResourceWindow.ResourceWindowSettings(i18N.get("lab.help-before"),
                resourceService.getHtmlData("content/", "helpBeforeLab.svg")));
    }
}
