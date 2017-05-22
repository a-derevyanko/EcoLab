package org.ekolab.server.service.impl.content.lab3;

import org.ekolab.server.dev.LogExecutionTime;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

import static java.lang.Math.pow;

/**
 * Created by 777Al on 24.04.2017.
 */
@Service
public class IsoLineChartServiceImpl implements IsoLineChartService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IsoLineChartServiceImpl.class);
    private static final Map<Integer, float[]> DASH_SETTINGS = new HashMap<>();
    private static final Map<Double[], XYDataset> DATASET_CACHE = new WeakHashMap<>();

    @Autowired
    private MessageSource messageSource;

    @PostConstruct
    private void init() {
        DASH_SETTINGS.put(0, new float[] {10.0f, 6.0f});
        DASH_SETTINGS.put(1, new float[] {6.0f, 6.0f});
        DASH_SETTINGS.put(2, new float[] {2.0f, 6.0f});
    }

    @Override
    @LogExecutionTime(500)
    public JFreeChart createIsoLineChart(Lab3Data data, Locale locale) {
        return createSplineChart(getDataSet(data), locale);
    }

    private XYDataset getDataSet(Lab3Data lab3Data) {
        Double windSpeedMaxGroundLevelConcentrationDistance = lab3Data.getWindSpeedMaxGroundLevelConcentrationDistance();
        Double harmfulSubstancesDepositionCoefficient = lab3Data.getHarmfulSubstancesDepositionCoefficient();
        Double windSpeed = lab3Data.getWindSpeed();
        Double[] key = new Double[]{windSpeedMaxGroundLevelConcentrationDistance,
                harmfulSubstancesDepositionCoefficient, windSpeed};
        return DATASET_CACHE.computeIfAbsent(key, k -> createDataset(windSpeedMaxGroundLevelConcentrationDistance,
                harmfulSubstancesDepositionCoefficient, windSpeed));
    }

    private XYDataset createDataset(Double windSpeedMaxGroundLevelConcentrationDistance,
                                      Double harmfulSubstancesDepositionCoefficient, Double windSpeed) {
        //todo
        /*windSpeedMaxGroundLevelConcentrationDistance = 200.0;
        harmfulSubstancesDepositionCoefficient = 0.5;
        windSpeed = 15.0;*/
        //todo

        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("isolineChart");

        dataset.addSeries(series);
        if (windSpeedMaxGroundLevelConcentrationDistance != null
                && harmfulSubstancesDepositionCoefficient != null  && windSpeed != null) {
            for (int x = 0; x < 100; x++) {
                for (double y : countY(x, windSpeedMaxGroundLevelConcentrationDistance,
                        harmfulSubstancesDepositionCoefficient, windSpeed)) {
                    series.add(x, y);
                    LOGGER.info("x = " + x + " y = " + y + " d = " + (double) x / windSpeedMaxGroundLevelConcentrationDistance);
                }
            }
        }

        return dataset;
    }

    private double countS1(double x, double windSpeedMaxGroundLevelConcentrationDistance,
                           double harmfulSubstancesDepositionCoefficient) {
        double d = x / windSpeedMaxGroundLevelConcentrationDistance;
        if (d <= 1) {
            return 3 * pow(d, 4) - 8 * pow(d, 3) + 6 * pow(d, 2);
        } else if (d <= 8) {
            return 1.13 / (0.13 * pow(d, 2) + 1);
        } else {
            if (harmfulSubstancesDepositionCoefficient > 1.5) {
                return 1 / (0.1 * pow(d, 2) + 2.47 * d - 17.8);
            } else {
                return d / (3.58 * pow(d, 2) - 35.2 * d + 120);
            }
        }
    }

    private double[] countT(double s1) {
        EquationFunction f = new QuarticFunction(45.1, 17, 12.8, 5, 1 - Math.sqrt(1 / s1));
        LOGGER.info("sqrt = " + Math.sqrt(1 / s1));
        double[] tValues = f.findRealRoots();
        return Arrays.stream(tValues).filter(x -> x > 0).toArray();
    }

    private double[] countY(double x, double windSpeedMaxGroundLevelConcentrationDistance,
                            double harmfulSubstancesDepositionCoefficient, double windSpeed) {
        double s1 = countS1(x, windSpeedMaxGroundLevelConcentrationDistance, harmfulSubstancesDepositionCoefficient);
        LOGGER.info(" s1 = " + s1);
        return Arrays.stream(countT(s1)).map(t -> Math.sqrt(t * x * x / windSpeed > 5 ? 5 : windSpeed)).toArray();
    }

    private JFreeChart createSplineChart(XYDataset dataSet, Locale locale) {
        // Create plot
        NumberAxis xAxis = new NumberAxis(messageSource.getMessage("lab3.isoline-x-axis", null, locale));
        NumberAxis yAxis = new NumberAxis(messageSource.getMessage("lab3.isoline-y-axis", null, locale));
        XYSplineRenderer renderer = new XYSplineRenderer();
        XYPlot plot = new XYPlot(dataSet, xAxis, yAxis, renderer);
        plot.setBackgroundPaint(null);
        plot.setDomainGridlinesVisible(false);
        plot.setDomainMinorGridlinesVisible(false);
        plot.setRangeGridlinesVisible(false);
        plot.setRangeMinorGridlinesVisible(false);
        //plot.setBackgroundImage(createStationInCityImage("map/moscow.svg", "map/moscow.svg"));
        //plot.setBackgroundImage(createStationInCityImage("map/moscow-min.png", "map/moscow-min.png"));
        try {
            plot.setBackgroundImage(ImageIO.read(IsoLineChartServiceImpl.class.getResourceAsStream("map/moscow-min1.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        plot.setAxisOffset(new RectangleInsets(4, 4, 4, 4));

        // Create chart
        JFreeChart chart = new JFreeChart(messageSource.getMessage("lab3.isoline-chart-title", null, locale),
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
                        1.0f, DASH_SETTINGS.get(i), 0.0f
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

    private static java.awt.Image createStationInCityImage(String cityMapImage, String stationImage) {
        try {
            BufferedImage bg = ImageIO.read(IsoLineChartServiceImpl.class.getResourceAsStream(cityMapImage));
            BufferedImage fg = ImageIO.read(IsoLineChartServiceImpl.class.getResourceAsStream(stationImage));
            Graphics2D g2d = bg.createGraphics();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            g2d.drawImage(fg, 0, 0, null);
            g2d.dispose();
            return bg;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
