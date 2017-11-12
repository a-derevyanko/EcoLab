package org.ekolab.client.vaadin.server.ui.view.content.lab_1;

import org.ekolab.client.vaadin.server.ui.common.LabWizard;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.ekolab.server.service.api.content.lab1.Lab1Service;

/**
 * Created by Андрей on 02.04.2017.
 */
public abstract class Lab1View<V extends Lab1Variant, S extends Lab1Service<V>> extends LabWizard<Lab1Data<V>, V, S> {
}
