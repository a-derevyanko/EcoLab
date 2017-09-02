package org.ekolab.client.vaadin.server.ui.view.api;

import org.ekolab.server.common.Role;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.security.RolesAllowed;

/**
 * Created by 777Al on 05.04.2017.
 */
public interface AutoSavableView extends SavableView {
    @Scheduled
    @RolesAllowed(Role.STUDENT)
    void autoSave();
}
