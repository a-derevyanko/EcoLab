package org.ekolab.server.service.impl.content.lab3;

import org.ekolab.server.model.content.lab3.Lab3Data;
import org.ekolab.server.service.api.content.lab3.IsoLineChartService;
import org.ekolab.server.service.impl.content.equations.ferrari.EquationFunction;
import org.ekolab.server.service.impl.content.equations.ferrari.QuarticFunction;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.pow;

/**
 * Created by 777Al on 24.04.2017.
 */
@Service
public class IsoLineChartServiceImpl implements IsoLineChartService {
    private static final Map<Integer, float[]> dashSettings = new HashMap<>();

    @PostConstruct
    private void init() {
        dashSettings.put(0, new float[] {10.0f, 6.0f});
        dashSettings.put(1, new float[] {6.0f, 6.0f});
        dashSettings.put(2, new float[] {2.0f, 6.0f});
    }

    @Override
    @Cacheable("LAB3_ISOLINE_CHART")
    public JFreeChart createIsoLineChart(Lab3Data data, String chartTitle, String xAxisLabel, String yAxisLabel) {
        return createSplineChart(createDataset(data), chartTitle, xAxisLabel, yAxisLabel);
    }

    /**
     * Returns a sample dataset.
     *
     * @return The dataset.
     */
    /*private static CategoryDataset createDataset(Lab3Data lab3Data) {

        // row keys...
        String series1 = "First";
        String series2 = "Second";
        String series3 = "Third";

        // column keys...
        String category1 = "Category 1";
        String category2 = "Category 2";
        String category3 = "Category 3";
        String category4 = "Category 4";
        String category5 = "Category 5";

        // create the dataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(1.0, series1, category1);
        dataset.addValue(4.0, series1, category2);
        dataset.addValue(3.0, series1, category3);
        dataset.addValue(5.0, series1, category4);
        dataset.addValue(5.0, series1, category5);

        dataset.addValue(5.0, series2, category1);
        dataset.addValue(7.0, series2, category2);
        dataset.addValue(6.0, series2, category3);
        dataset.addValue(8.0, series2, category4);
        dataset.addValue(4.0, series2, category5);

        dataset.addValue(4.0, series3, category1);
        dataset.addValue(3.0, series3, category2);
        dataset.addValue(2.0, series3, category3);
        dataset.addValue(3.0, series3, category4);
        dataset.addValue(6.0, series3, category5);

        return dataset;
    }*/
    private XYDataset createDataset(Lab3Data lab3Data) {

        // Convert latencyMap to XYDataset
        XYSeries series = new XYSeries("isolineChart");

        //for (int x = 0; x < )
        /*for (long time : latencyMap.keySet()) {
            series.add(time, Math.abs(latencyMap.get(time).getLatency()));
        }*/
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        return dataset;
    }

    private double countS1(double x, Lab3Data data) {
        double d = x / data.getWindSpeedMaxGroundLevelConcentrationDistance();
        if (d <= 1) {
            return 3 * pow(d, 4) - 8 * pow(d, 3) + 6 * pow(d, 2);
        } else if (d <= 8) {
            return 1.13 / (0.13 * pow(d, 2) + 1);
        } else {
            if (data.getHarmfulSubstancesDepositionCoefficient() > 1.5) {
                return d / (0.1 * pow(d, 2) + 2.47 * d - 17.8);
            } else {
                return d / (3.58 * pow(d, 2) - 35.2 * d + 120);
            }
        }
    }

    private double countS2(double x, double y, Lab3Data data) {
        double t = data.getWindSpeed() > 5 ?
                data.getWindSpeed() * pow(y, 2) / pow(x, 2) :
                5 * pow(y, 2) / pow(x, 2);

        return 1 / (1 + 5 * t + 12.8 * pow(t, 2) + 17 * pow(t, 3) + 45.1 * pow(t, 4));
    }

    private double[] countT(double s2) {
        EquationFunction f = new QuarticFunction(45.1, 17, 12.8, 5, 1 - Math.sqrt(1 / s2));
        return f.findRealRoots();
    }

    private JFreeChart createSplineChart(XYDataset dataSet, String chartTitle, String xAxisLabel, String yAxisLabel) {
        // Create plot
        NumberAxis xAxis = new NumberAxis(xAxisLabel);
        NumberAxis yAxis = new NumberAxis(yAxisLabel);
        XYSplineRenderer renderer = new XYSplineRenderer();
        XYPlot plot = new XYPlot(dataSet, xAxis, yAxis, renderer);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        plot.setAxisOffset(new RectangleInsets(4, 4, 4, 4));

        // Create chart
        JFreeChart chart = new JFreeChart(chartTitle,
                JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        chart.getLegend().setPosition(RectangleEdge.LEFT);
        ChartUtilities.applyCurrentTheme(chart);

        for (int i = 0; i < dataSet.getSeriesCount(); i++) {
            stylizeSeriesStroke(plot, i);
        }

        // Draw png
        /*BufferedImage bi = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_BGR);
        Graphics graphics = bi.getGraphics();
        chartPanel.setBounds(0, 0, width, height);
        chartPanel.paint(graphics);
        ImageIO.write(bi, "png", new File(filename));*/
        return chart;
    }

    private void stylizeSeriesStroke(XYPlot plot, int i) {
        plot.getRenderer().setSeriesStroke(i, new BasicStroke(
                        2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, dashSettings.get(i), 0.0f
                )
        );
    }

    private JFreeChart createChart(CategoryDataset dataset) throws IOException {

        // create the chart...
        JFreeChart chart = ChartFactory.createBarChart("Bar Chart Demo 1", // chart
                // title
                "Category", // domain axis label
                "Value", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips?
                false // URLs?
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setBackgroundImage(
                createStationInCityImage(
                        ImageIO.read(IsoLineChartServiceImpl.class.getResourceAsStream("img/logo.svg")),
                        ImageIO.read(IsoLineChartServiceImpl.class.getResourceAsStream("img/circle.gif"))));
        plot.setDomainGridlinePaint(Color.white);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.white);

        // ******************************************************************
        // More than 150 demo applications are included with the JFreeChart
        // Developer Guide...for more information, see:
        //
        // > http://www.object-refinery.com/jfreechart/guide.html
        //
        // ******************************************************************

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        // renderer.setDrawBarOutline(false);

        // set up gradient paints for series...
        GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue, 0.0f,
                0.0f, new Color(0, 0, 64));
        GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.green, 0.0f,
                0.0f, new Color(0, 64, 0));
        GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.red, 0.0f,
                0.0f, new Color(64, 0, 0));
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions
                .createUpRotationLabelPositions(Math.PI / 6.0));
        // OPTIONAL CUSTOMISATION COMPLETED.

        return chart;

    }

    private static java.awt.Image createStationInCityImage(final BufferedImage bg, BufferedImage fg) {
        Graphics2D g2d = bg.createGraphics();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        g2d.drawImage(fg, 0, 0, null);
        g2d.dispose();
        return bg;
    }
}
