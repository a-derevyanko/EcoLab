package org.ekolab.server.service.impl.content.lab1;

import org.ekolab.server.dao.api.content.lab1.Lab1Dao;
import org.ekolab.server.model.content.LabVariant;
import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.ekolab.server.service.api.content.LabChartType;
import org.ekolab.server.service.api.content.lab1.Lab1Service;
import org.ekolab.server.service.impl.content.LabServiceImpl;
import org.jfree.chart.JFreeChart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Created by 777Al on 26.04.2017.
 */
@Service
public class Lab1ServiceImpl extends LabServiceImpl<Lab1Data, Lab1Variant> implements Lab1Service {

    @Autowired
    public Lab1ServiceImpl(Lab1Dao lab1Dao) {
        super(lab1Dao);
    }

    @Override
    public byte[] createReport(Lab1Data labData, Locale locale) {
        return new byte[0];
    }

    @Override
    public Lab1Data updateCalculatedFields(Lab1Data labData) {
        return null;
    }

    @Override
    public Lab1Data createNewLabData() {
        return new Lab1Data();
    }

    @Override
    public byte[] printInitialData(LabVariant variant, Locale locale) {
        return new byte[0];
    }

    @Override
    public JFreeChart createChart(Lab1Data labData, Locale locale, LabChartType chartType) {
        return null;
    }

    @Override
    protected Lab1Variant generateNewLabVariant() {
        Lab1Variant variant = new Lab1Variant();
        //todo
        return variant;
    }
}
