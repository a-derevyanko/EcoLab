package org.ekolab.server.service.impl.content.lab1.experiment;

import org.ekolab.server.dao.api.content.lab1.experiment.Lab1ExperimentDao;
import org.ekolab.server.model.content.lab1.experiment.Lab1ExperimentLog;
import org.ekolab.server.service.api.ReportService;
import org.ekolab.server.service.api.StudentInfoService;
import org.ekolab.server.service.api.UserInfoService;
import org.ekolab.server.service.api.content.lab1.experiment.Lab1ExperimentService;
import org.ekolab.server.service.impl.content.lab1.Lab1ServiceImpl;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * Created by 777Al on 26.04.2017.
 */
@Service
public class Lab1ExperimentServiceImpl extends Lab1ServiceImpl<Lab1ExperimentLog, Lab1ExperimentDao> implements Lab1ExperimentService {

    public Lab1ExperimentServiceImpl(Lab1ExperimentDao labDao, @Lazy UserInfoService userInfoService, ReportService reportService, MessageSource messageSource, StudentInfoService studentInfoService) {
        super(labDao, userInfoService, reportService, messageSource, studentInfoService);
    }

    @Override
    protected Lab1ExperimentLog generateNewLabVariant() {
        Lab1ExperimentLog variant = new Lab1ExperimentLog();
        variant.setName("");
        return variant;
    }

    @Override
    public Lab1ExperimentLog updateExperimentJournal(Lab1ExperimentLog experimentJournal) {
        labDao.updateExperimentJournal(experimentJournal);
        return experimentJournal;
    }
}
