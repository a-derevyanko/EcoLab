package org.ekolab.client.vaadin.server.ui.view.content.lab_1.random;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.client.vaadin.server.ui.customcomponents.ParameterWithFormulaeLayout;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.Lab1Step3;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1RandomVariant;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringComponent
@ViewScope
public class Lab1RandomStep3 extends Lab1Step3<Lab1RandomVariant> {
    public Lab1RandomStep3(I18N i18N, ParameterWithFormulaeLayout<Lab1Data<Lab1RandomVariant>, Lab1RandomVariant> firstFormLayout) {
        super(i18N, firstFormLayout);
    }
}
