package org.ekolab.server.service.api.content.lab1.experiment;

import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1ExperimentLog;
import org.ekolab.server.service.api.content.LabExperimentService;
import org.ekolab.server.service.api.content.lab1.Lab1Service;

public interface Lab1ExperimentService extends Lab1Service<Lab1ExperimentLog>, LabExperimentService<Lab1Data<Lab1ExperimentLog>, Lab1ExperimentLog> {
}
