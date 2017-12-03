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

/**
 * Created by 777Al on 26.04.2017.
 */
public abstract class Lab2ServiceImpl<V extends Lab2Variant, D extends Lab2Dao<V>> extends LabServiceImpl<Lab2Data<V>, V, D> implements Lab2Service<V> {

    protected Lab2ServiceImpl(D labDao, UserInfoService userInfoService, ReportService reportService, MessageSource messageSource, StudentInfoService studentInfoService) {
        super(labDao, userInfoService, reportService, messageSource, studentInfoService);
    }

    @Override
    protected Lab2Data<V> createNewLabData() {
        return new Lab2Data<>();
    }

    @Override
    public void updateCalculatedFields(Lab2Data labData) {
    }


    @Override
    public int getLabNumber() {
        return 1;
    }
}
