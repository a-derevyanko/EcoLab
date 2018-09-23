package org.ecolab.server.service.api.content.lab3;

import org.ecolab.server.model.content.lab3.Lab3Data;
import org.ecolab.server.model.content.lab3.Lab3Variant;
import org.ecolab.server.service.api.content.LabService;
import org.jfree.chart.JFreeChart;

/**
 * Created by 777Al on 26.04.2017.
 */
public interface Lab3Service extends LabService<Lab3Data, Lab3Variant> {
    JFreeChart createChart(Lab3Data labData, Lab3ChartType chartType);
}
