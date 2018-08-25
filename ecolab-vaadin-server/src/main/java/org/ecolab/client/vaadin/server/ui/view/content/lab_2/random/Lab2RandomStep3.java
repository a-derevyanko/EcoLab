package org.ecolab.client.vaadin.server.ui.view.content.lab_2.random;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.client.vaadin.server.ui.view.content.lab_2.Lab2Step3;
import org.ecolab.server.model.content.lab2.Lab2Data;
import org.ecolab.server.model.content.lab2.random.Lab2RandomVariant;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringComponent
@ViewScope
public class Lab2RandomStep3 extends Lab2Step3<Lab2RandomVariant> {
    // ----------------------------- Графические компоненты --------------------------------

    public Lab2RandomStep3(I18N i18N, Binder<Lab2Data<Lab2RandomVariant>> binder) {
        super(i18N, binder);
    }
}
