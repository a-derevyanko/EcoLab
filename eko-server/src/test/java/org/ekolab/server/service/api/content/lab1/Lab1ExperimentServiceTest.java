package org.ekolab.server.service.api.content.lab1;

import org.ekolab.server.model.content.lab1.experiment.Lab1ExperimentLog;
import org.ekolab.server.service.api.content.lab1.experiment.Lab1ExperimentService;

/**
 * Created by Андрей on 01.05.2017.
 */
public class Lab1ExperimentServiceTest extends Lab1ServiceTest<Lab1ExperimentLog> {
    public Lab1ExperimentServiceTest(Lab1ExperimentService lab1Service) {
        super(lab1Service);
    }
}