package org.ekolab.server.service.impl.content.lab1.experiment;

import org.ekolab.server.dao.api.content.lab1.experiment.Lab1ExperimentDao;
import org.ekolab.server.model.content.lab1.Lab1ExperimentLog;
import org.ekolab.server.service.api.content.lab1.experiment.Lab1ExperimentService;
import org.ekolab.server.service.impl.content.lab1.Lab1ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 777Al on 26.04.2017.
 */
@Service
public class Lab1ExperimentServiceImpl extends Lab1ServiceImpl<Lab1ExperimentLog> implements Lab1ExperimentService {

    @Autowired
    public Lab1ExperimentServiceImpl(Lab1ExperimentDao lab1Dao) {
        super(lab1Dao);
    }

    @Override
    protected Lab1ExperimentLog generateNewLabVariant() {
        return new Lab1ExperimentLog();
    }
}
