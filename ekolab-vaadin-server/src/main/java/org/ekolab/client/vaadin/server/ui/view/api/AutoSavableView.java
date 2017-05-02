package org.ekolab.client.vaadin.server.ui.view.api;

import org.ekolab.server.common.Authorize;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Created by 777Al on 05.04.2017.
 */
public interface AutoSavableView extends SavableView {
    @Scheduled
    @PreAuthorize(Authorize.HasAuthorities.STUDENT)
    void autoSave();
}
