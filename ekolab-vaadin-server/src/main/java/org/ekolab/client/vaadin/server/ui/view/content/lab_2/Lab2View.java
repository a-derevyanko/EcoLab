package org.ekolab.client.vaadin.server.ui.view.content.lab_2;

import org.ekolab.client.vaadin.server.ui.common.LabWizard;
import org.ekolab.client.vaadin.server.ui.view.api.AutoSavableView;
import org.ekolab.server.model.content.lab2.Lab2Data;
import org.ekolab.server.model.content.lab2.Lab2Variant;
import org.ekolab.server.service.api.content.lab2.Lab2Service;

/**
 * Created by Андрей on 02.04.2017.
 */
public abstract class Lab2View extends LabWizard<Lab2Data, Lab2Variant, Lab2Service> implements AutoSavableView {
}
