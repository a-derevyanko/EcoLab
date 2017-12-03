package org.ekolab.server.dao.api.content.lab2.experiment;


import org.ekolab.server.dao.api.content.LabExperimentDao;
import org.ekolab.server.dao.api.content.lab2.Lab2Dao;
import org.ekolab.server.model.content.lab2.Lab2Data;
import org.ekolab.server.model.content.lab2.experiment.Lab2ExperimentLog;

public interface Lab2ExperimentDao extends Lab2Dao<Lab2ExperimentLog>,
        LabExperimentDao<Lab2Data<Lab2ExperimentLog>, Lab2ExperimentLog> {
}
