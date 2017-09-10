package org.ekolab.server;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.StandardChartTheme;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.awt.*;

/**
 * Created by 777Al on 24.04.2017.
 */
@SpringBootConfiguration
public class ServerConfiguration {
    @EventListener(ApplicationReadyEvent.class)
    private void createDefaultChartTheme() {
        StandardChartTheme chartTheme = new StandardChartTheme("EkoLab");

        chartTheme.setChartBackgroundPaint(Color.WHITE);
        chartTheme.setPlotBackgroundPaint(Color.WHITE);
        chartTheme.setPlotOutlinePaint(Color.WHITE);
        chartTheme.setDomainGridlinePaint(Color.LIGHT_GRAY);
        chartTheme.setRangeGridlinePaint(Color.LIGHT_GRAY);

        ChartFactory.setChartTheme(chartTheme);
    }

}
