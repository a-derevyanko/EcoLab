package org.ekolab.server.service.api.content.lab3;

import org.ekolab.server.model.content.lab3.Lab3Data;
import org.ekolab.server.model.content.lab3.Lab3Variant;
import org.ekolab.server.service.api.content.LabService;
import org.jfree.chart.JFreeChart;

import java.util.Locale;

/**
 * Created by 777Al on 26.04.2017.
 */
public interface Lab3Service extends LabService<Lab3Data, Lab3Variant> {
    JFreeChart createChart(Lab3Data labData, Locale locale, Lab3ChartType chartType);
}
