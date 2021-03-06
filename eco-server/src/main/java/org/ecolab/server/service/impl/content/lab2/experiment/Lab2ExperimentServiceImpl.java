package org.ecolab.server.service.impl.content.lab2.experiment;

import org.apache.commons.lang.math.RandomUtils;
import org.ecolab.server.dao.api.content.lab2.experiment.Lab2ExperimentDao;
import org.ecolab.server.model.content.lab2.Frequency;
import org.ecolab.server.model.content.lab2.experiment.Lab2ExperimentLog;
import org.ecolab.server.service.api.ReportService;
import org.ecolab.server.service.api.StudentInfoService;
import org.ecolab.server.service.api.UserInfoService;
import org.ecolab.server.service.api.content.lab2.experiment.Lab2ExperimentService;
import org.ecolab.server.service.impl.content.lab2.Lab2ServiceImpl;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;

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
        var variant = new Lab2ExperimentLog();
        variant.setEstimatedGeometricMeanFrequency(Frequency.
                values()[RandomUtils.nextInt(Frequency.values().length)]);
        variant.setSoundPressure(Collections.nCopies(8, new ArrayList<>(Collections.nCopies(9, 0.0))));
        variant.setAverageSoundPressure(new ArrayList<>(Collections.nCopies(9, 0.0)));
        return variant;
    }

    @Override
    public Lab2ExperimentLog updateExperimentJournal(Lab2ExperimentLog experimentJournal) {
        labDao.updateExperimentJournal(experimentJournal);
        return experimentJournal;
    }
}
