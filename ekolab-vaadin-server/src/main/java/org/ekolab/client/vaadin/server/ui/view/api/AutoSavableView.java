package org.ekolab.client.vaadin.server.ui.view.api;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by 777Al on 05.04.2017.
 */
public interface AutoSavableView extends SavableView {
    @Scheduled(fixedRateString = "${lab.autoSaveRate:#{60000}}", initialDelayString = "${lab.autoSaveRate:#{60000}}")
    @Async
    default void autoSave() {
        saveData();
    }
}
