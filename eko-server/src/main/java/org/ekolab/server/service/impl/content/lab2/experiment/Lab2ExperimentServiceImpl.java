package org.ekolab.server.service.impl.content.lab2.experiment;

import org.ekolab.server.dao.api.content.lab2.experiment.Lab2ExperimentDao;
import org.ekolab.server.model.content.lab2.experiment.Lab2ExperimentLog;
import org.ekolab.server.service.api.ReportService;
import org.ekolab.server.service.api.StudentInfoService;
import org.ekolab.server.service.api.UserInfoService;
import org.ekolab.server.service.api.content.lab2.experiment.Lab2ExperimentService;
import org.ekolab.server.service.impl.content.lab2.Lab2ServiceImpl;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * Created by 777Al on 26.04.2017.
 */
@Service
public class Lab2ExperimentServiceImpl extends Lab2ServiceImpl<Lab2ExperimentLog, Lab2ExperimentDao> implements Lab2ExperimentService {

    public Lab2ExperimentServiceImpl(Lab2ExperimentDao labDao, @Lazy UserInfoService userInfoService, ReportService reportService, MessageSource messageSource, StudentInfoService studentInfoService) {
        super(labDao, userInfoService, reportService, messageSource, studentInfoService);
    }

    @Override
    protected Lab2ExperimentLog generateNewLabVariant() {
        return new Lab2ExperimentLog();
    }

    @Override
    public Lab2ExperimentLog updateExperimentJournal(Lab2ExperimentLog experimentJournal) {
        labDao.updateExperimentJournal(experimentJournal);
        return experimentJournal;
    }
}
