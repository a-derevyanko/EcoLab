package org.ekolab.client.vaadin.server.ui.demo;

import com.vaadin.spring.annotation.SpringView;
import org.ekolab.client.vaadin.server.ui.view.content.lab_1.experiment.Lab1ExperimentView;
import org.ekolab.server.common.Profiles;
import org.springframework.context.annotation.Profile;

/**
 * Created by Андрей on 02.04.2017.
 */
@SpringView(name = DemoLab1ExperimentView.NAME)
@Profile(value = {Profiles.MODE.DEMO, Profiles.MODE.DEV})
public class DemoLab1ExperimentView extends Lab1ExperimentView {
}
