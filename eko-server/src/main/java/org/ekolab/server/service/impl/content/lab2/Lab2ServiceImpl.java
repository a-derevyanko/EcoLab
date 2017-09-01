package org.ekolab.server.service.impl.content.lab2;

import org.ekolab.server.dao.api.content.lab2.Lab2Dao;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab2.Lab2Data;
import org.ekolab.server.model.content.lab2.Lab2Variant;
import org.ekolab.server.service.api.content.LabChartType;
import org.ekolab.server.service.api.content.lab2.Lab2Service;
import org.ekolab.server.service.impl.content.LabServiceImpl;
import org.jfree.chart.JFreeChart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Created by 777Al on 26.04.2017.
 */
@Service
public class Lab2ServiceImpl extends LabServiceImpl<Lab2Data, Lab2Variant> implements Lab2Service {
    @Autowired
    public Lab2ServiceImpl(Lab2Dao lab2Dao) {
        super(lab2Dao);
    }

    @Override
    public byte[] createReport(Lab2Data labData, Locale locale) {
        return new byte[0];
    }

    @Override
    public Lab2Data updateCalculatedFields(Lab2Data labData) {
        //todo correctionFactor!!!!
        return null;
    }

    @Override
    public JFreeChart createChart(Lab2Data labData, Locale locale, LabChartType chartType) {
        return null;
    }

    @Override
    public Lab2Data createNewLabData() {
        return new Lab2Data();
    }

    @Override
    public int getLabNumber() {
        return 1;
    }

    @Override
    protected Lab2Variant generateNewLabVariant() {

        return null;
    }

    protected boolean validateFieldValue(String fieldName, Object value, Lab1Data labData) {
        return false;
    }
}
