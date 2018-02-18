package org.ekolab.client.vaadin.server.ui.view.content.lab_2.random;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import org.ekolab.client.vaadin.server.service.api.ResourceService;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.customcomponents.ParameterWithFormulaeLayout;
import org.ekolab.client.vaadin.server.ui.view.content.lab_2.Lab2Step2;
import org.ekolab.client.vaadin.server.ui.windows.ResourceWindow;
import org.ekolab.server.model.content.lab2.Lab2Data;
import org.ekolab.server.model.content.lab2.random.Lab2RandomVariant;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringComponent
@ViewScope
public class Lab2RandomStep2 extends Lab2Step2<Lab2RandomVariant> {
    // ----------------------------- Графические компоненты --------------------------------

    public Lab2RandomStep2(I18N i18N, ResourceWindow resourceWindow, ResourceService resourceService, Binder<Lab2Data<Lab2RandomVariant>> binder, ParameterWithFormulaeLayout<Lab2Data<Lab2RandomVariant>, Lab2RandomVariant> firstFormLayout) {
        super(i18N, resourceWindow, resourceService, binder, firstFormLayout);
    }
}
