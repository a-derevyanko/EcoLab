package org.ekolab.client.vaadin.server.ui.view.content.lab_1;

import org.ekolab.client.vaadin.server.ui.common.LabWizard;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;

/**
 * Created by Андрей on 02.04.2017.
 */
public abstract class Lab1View<T extends Lab1Data<V>, V extends Lab1Variant> extends LabWizard<T, V> {
}
