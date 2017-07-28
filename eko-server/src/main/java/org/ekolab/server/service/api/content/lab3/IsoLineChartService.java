package org.ekolab.server.service.api.content.lab3;

import org.ekolab.server.model.content.lab3.Lab3Data;
import org.ekolab.server.service.api.content.LabChartType;
import org.jfree.chart.JFreeChart;

import java.util.Locale;

/**
 * Created by 777Al on 24.04.2017.
 */
public interface IsoLineChartService {
    JFreeChart createIsoLineChart(Lab3Data lab3Data, Locale locale, LabChartType chartType);
}
