package org.ecolab.server;

import java.awt.Color;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.StandardChartTheme;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

/**
 * Created by 777Al on 24.04.2017.
 */
@SpringBootConfiguration
public class ServerConfiguration {
    @EventListener(ApplicationReadyEvent.class)
    private void createDefaultChartTheme() {
        StandardChartTheme chartTheme = new StandardChartTheme("EcoLab");

        chartTheme.setChartBackgroundPaint(Color.WHITE);
        chartTheme.setPlotBackgroundPaint(Color.WHITE);
        chartTheme.setPlotOutlinePaint(Color.WHITE);
        chartTheme.setDomainGridlinePaint(Color.DARK_GRAY);
        chartTheme.setRangeGridlinePaint(Color.DARK_GRAY);

        ChartFactory.setChartTheme(chartTheme);
    }
}
