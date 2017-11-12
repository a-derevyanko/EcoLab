package org.ekolab.client.vaadin.server.ui.view.content.lab_1.random;

import com.vaadin.spring.annotation.SpringView;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.Lab1Step2;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.Lab1Step3;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.Lab1View;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.ekolab.server.service.api.content.lab1.random.Lab1RandomService;

/**
 * Created by Андрей on 02.04.2017.
 */
@SpringView(name = Lab1RandomDataView.NAME)
public class Lab1RandomDataView extends Lab1View<Lab1Variant, Lab1RandomService> {
    public static final String NAME = "lab1randomdata";

    public Lab1RandomDataView(Lab1RandomPresentationStep presentationStep, Lab1RandomStep1 step1, Lab1Step2 step2, Lab1Step3 step3) {
        labSteps.add(presentationStep);
        labSteps.add(step1);
        labSteps.add(step2);
        labSteps.add(step3);
    }

    // ----------------------------- Графические компоненты --------------------------------
}
