package org.ekolab.client.vaadin.server.ui.view.content.lab_2.experiment;

import com.vaadin.spring.annotation.SpringView;
import org.ekolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ekolab.client.vaadin.server.ui.view.content.lab_2.Lab2View;
import org.ekolab.server.common.Profiles;
import org.springframework.context.annotation.Profile;

import java.util.Collection;

/**
 * Created by Андрей on 02.04.2017.
 */
@SpringView(name = Lab2ExperimentView.NAME)
@Profile(Profiles.MODE.PROD)
public class Lab2ExperimentView extends Lab2View {
    public static final String NAME = "lab2experiment";

    @Override
    protected Collection<LabWizardStep> getLabSteps() {
        return null;
    }
}
