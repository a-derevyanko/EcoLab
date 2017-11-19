package org.ekolab.server.service.impl.content.lab2;

import org.ekolab.server.dao.api.content.lab2.Lab2Dao;
import org.ekolab.server.model.content.lab2.Lab2Data;
import org.ekolab.server.model.content.lab2.Lab2Variant;
import org.ekolab.server.service.api.ReportService;
import org.ekolab.server.service.api.StudentInfoService;
import org.ekolab.server.service.api.UserInfoService;
import org.ekolab.server.service.api.content.lab2.Lab2Service;
import org.ekolab.server.service.impl.content.LabServiceImpl;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

/**
 * Created by 777Al on 26.04.2017.
 */
@Service
public class Lab2ServiceImpl extends LabServiceImpl<Lab2Data, Lab2Variant, Lab2Dao> implements Lab2Service {
    protected Lab2ServiceImpl(Lab2Dao labDao, UserInfoService userInfoService, ReportService reportService, MessageSource messageSource, StudentInfoService studentInfoService) {
        super(labDao, userInfoService, reportService, messageSource, studentInfoService);
    }

    @Override
    public void updateCalculatedFields(Lab2Data labData) {
        //todo correctionFactor!!!!
    }

    @Override
    public Lab2Data createNewLabData() {
        return new Lab2Data();
    }

    @Override
    public int getLabNumber() {
        return 2;
    }

    @Override
    protected Lab2Variant generateNewLabVariant() {
        return null;
    }
}
