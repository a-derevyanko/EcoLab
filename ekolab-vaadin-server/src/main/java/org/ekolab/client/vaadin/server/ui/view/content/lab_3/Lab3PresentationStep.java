package org.ekolab.client.vaadin.server.ui.view.content.lab_3;

import com.github.lotsabackscatter.blueimp.gallery.Image;
import com.vaadin.spring.annotation.SpringView;
import org.ekolab.client.vaadin.server.ui.common.LabPresentationStep;

import java.util.List;

/**
 * Created by 777Al on 03.04.2017.
 */
@SpringView
public class Lab3PresentationStep extends LabPresentationStep {
    // ---------------------------- Графические компоненты --------------------
    @Override
    protected List<Image> getPresentationSlides() {
        return resourceService.svg("img/icon.svg",
                "img/logo.svg",
                "img/logoCrop.svg",
                "img/textLogo.svg"
        );
    }

    @Override
    public boolean onBack() {
        return false;
    }
}
