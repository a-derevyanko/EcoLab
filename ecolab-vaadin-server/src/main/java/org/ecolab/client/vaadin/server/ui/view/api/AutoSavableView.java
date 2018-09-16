package org.ecolab.client.vaadin.server.ui.view.api;

import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by 777Al on 05.04.2017.
 */
public interface AutoSavableView extends SavableView {
    @Scheduled
    void autoSave();
}
