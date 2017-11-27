package org.ekolab.server.service.api.content.lab2.experiment;

import org.ekolab.server.model.content.lab2.Lab2Data;
import org.ekolab.server.model.content.lab2.Lab2ExperimentLog;
import org.ekolab.server.service.api.content.LabExperimentService;
import org.ekolab.server.service.api.content.lab2.Lab2Service;

public interface Lab2ExperimentService extends Lab2Service<Lab2ExperimentLog>, LabExperimentService<Lab2Data<Lab2ExperimentLog>, Lab2ExperimentLog> {
}
