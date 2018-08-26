package org.ecolab.server.service.api.content.lab1.experiment;

import org.ecolab.server.model.content.lab1.Lab1Data;
import org.ecolab.server.model.content.lab1.experiment.Lab1ExperimentLog;
import org.ecolab.server.service.api.content.LabExperimentService;
import org.ecolab.server.service.api.content.lab1.Lab1Service;

public interface Lab1ExperimentService extends Lab1Service<Lab1ExperimentLog>, LabExperimentService<Lab1Data<Lab1ExperimentLog>, Lab1ExperimentLog> {
}
