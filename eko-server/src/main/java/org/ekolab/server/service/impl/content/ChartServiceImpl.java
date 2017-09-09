package org.ekolab.server.service.impl.content;

import org.ekolab.server.service.api.content.ChartService;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.StandardChartTheme;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.awt.*;

/**
 * Created by 777Al on 24.04.2017.
 */
@Service
public class ChartServiceImpl implements ChartService {
    @PostConstruct
    private void createDefaultTheme() {
        StandardChartTheme chartTheme = new StandardChartTheme("EkoLab");

        chartTheme.setChartBackgroundPaint(Color.WHITE);
        chartTheme.setPlotBackgroundPaint(Color.WHITE);
        chartTheme.setPlotOutlinePaint(Color.WHITE);
        chartTheme.setDomainGridlinePaint(Color.LIGHT_GRAY);
        chartTheme.setRangeGridlinePaint(Color.LIGHT_GRAY);

        ChartFactory.setChartTheme(chartTheme);
    }

}
