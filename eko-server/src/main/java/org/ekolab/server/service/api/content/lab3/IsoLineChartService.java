package org.ekolab.server.service.api.content.lab3;

import org.ekolab.server.model.content.lab3.Lab3Data;
import org.jfree.chart.JFreeChart;

/**
 * Created by 777Al on 24.04.2017.
 */
public interface IsoLineChartService {
    JFreeChart createIsoLineChart(Lab3Data data, String chartTitle, String xAxisLabel, String yAxisLabel);
}
