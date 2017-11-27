package org.ekolab.server.service.impl.content.lab2.random;

import org.apache.commons.lang.math.RandomUtils;
import org.ekolab.server.dao.api.content.lab2.random.Lab2RandomDao;
import org.ekolab.server.model.content.DataValue;
import org.ekolab.server.model.content.lab2.Lab2RandomVariant;
import org.ekolab.server.service.api.ReportService;
import org.ekolab.server.service.api.StudentInfoService;
import org.ekolab.server.service.api.UserInfoService;
import org.ekolab.server.service.api.content.lab2.random.Lab2RandomService;
import org.ekolab.server.service.impl.content.lab2.Lab2ServiceImpl;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;

/**
 * Created by 777Al on 26.04.2017.
 */
@Service
public class Lab2RandomServiceImpl extends Lab2ServiceImpl<Lab2RandomVariant, Lab2RandomDao> implements Lab2RandomService {
    protected Lab2RandomServiceImpl(Lab2RandomDao labDao, UserInfoService userInfoService, ReportService reportService, MessageSource messageSource, StudentInfoService studentInfoService) {
        super(labDao, userInfoService, reportService, messageSource, studentInfoService);
    }


    @Override
    protected Map<String, Object> getInitialDataWithLocalizedValues(Lab2RandomVariant data, Locale locale) {
        Map<String, Object> map = super.getInitialDataWithLocalizedValues(data, locale);
        map.remove("city");

        return map;
    }

    @Override
    protected Lab2RandomVariant generateNewLabVariant() {
        Lab2RandomVariant variant = new Lab2RandomVariant();

        return variant;
    }

}
