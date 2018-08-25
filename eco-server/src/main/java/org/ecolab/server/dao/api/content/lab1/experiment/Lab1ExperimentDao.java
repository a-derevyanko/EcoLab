package org.ecolab.server.dao.api.content.lab1.experiment;

import org.ecolab.server.dao.api.content.LabExperimentDao;
import org.ecolab.server.dao.api.content.lab1.Lab1Dao;
import org.ecolab.server.model.content.lab1.Lab1Data;
import org.ecolab.server.model.content.lab1.experiment.Lab1ExperimentLog;

public interface Lab1ExperimentDao extends Lab1Dao<Lab1ExperimentLog>,
        LabExperimentDao<Lab1Data<Lab1ExperimentLog>, Lab1ExperimentLog> {
}
