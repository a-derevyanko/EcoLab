package org.ecolab.server.service.api.content.lab2.experiment;

import org.ecolab.server.model.content.lab2.Lab2Data;
import org.ecolab.server.model.content.lab2.experiment.Lab2ExperimentLog;
import org.ecolab.server.service.api.content.LabExperimentService;
import org.ecolab.server.service.api.content.lab2.Lab2Service;

public interface Lab2ExperimentService extends Lab2Service<Lab2ExperimentLog>, LabExperimentService<Lab2Data<Lab2ExperimentLog>, Lab2ExperimentLog> {
}
