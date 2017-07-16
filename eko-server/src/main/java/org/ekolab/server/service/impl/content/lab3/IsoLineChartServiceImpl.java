package org.ekolab.server.service.impl.content.lab3;

import org.ekolab.server.common.MathUtils;
import org.ekolab.server.dev.LogExecutionTime;
import org.ekolab.server.service.api.content.lab3.IsoLineChartService;
import org.ekolab.server.service.impl.content.equations.ferrari.EquationFunction;
import org.ekolab.server.service.impl.content.equations.ferrari.QuarticFunction;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.WeakHashMap;

import static java.lang.Math.pow;

/**
 * Created by 777Al on 24.04.2017.
 */
@Service
public class IsoLineChartServiceImpl implements IsoLineChartService {
    private static final int MAX_DISTANCE = 35000;
    private static final int MAX_RIGHT_SPACE = 2000;
    private static final int BIG_SERIES_STEP = 100;
    private static final int SMALL_SERIES_STEP = 10;
    private static final Logger LOGGER = LoggerFactory.getLogger(IsoLineChartServiceImpl.class);
    private static final Map<Double[], XYDataset> DATASET_CACHE = new WeakHashMap<>();

    @Autowired
    private MessageSource messageSource;

    @Override
    @LogExecutionTime(500)
    public JFreeChart createIsoLineChart(double windSpeedMaxGroundLevelConcentrationDistance,
                                         double harmfulSubstancesDepositionCoefficient, double groundLevelConcentration,
                                         double backgroundConcentration, double windSpeed, double mac, Locale locale) {
        return createSplineChart(getDataSet(windSpeedMaxGroundLevelConcentrationDistance,
        harmfulSubstancesDepositionCoefficient, groundLevelConcentration,
        backgroundConcentration, windSpeed, mac), locale);
    }

    private XYDataset getDataSet(double windSpeedMaxGroundLevelConcentrationDistance,
                                 double harmfulSubstancesDepositionCoefficient, double groundLevelConcentration,
                                 double no2BackgroundConcentration, double windSpeed, double mac) {

        Double[] key = new Double[]{windSpeedMaxGroundLevelConcentrationDistance,
                harmfulSubstancesDepositionCoefficient, windSpeed, groundLevelConcentration};

        return DATASET_CACHE.computeIfAbsent(key, k -> createDataset(
                windSpeedMaxGroundLevelConcentrationDistance,
                harmfulSubstancesDepositionCoefficient, groundLevelConcentration, no2BackgroundConcentration, windSpeed, mac));
    }

    private XYDataset createDataset(double windSpeedMaxGroundLevelConcentrationDistance,
                                    double harmfulSubstancesDepositionCoefficient, double groundLevelConcentration,
                                    double backgroundConcentration, double windSpeed, double mac) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        int Xm = Math.toIntExact(Math.round(windSpeedMaxGroundLevelConcentrationDistance));

        for (double CyCoefficient = 0.1; CyCoefficient < 0.95; CyCoefficient += 0.1) {
            double C = CyCoefficient * groundLevelConcentration;
            if (C > backgroundConcentration) {
                XYSeries series = new XYSeries("C = " + new BigDecimal(CyCoefficient).setScale(1, RoundingMode.HALF_UP).doubleValue() + " Cm", false);
                dataset.addSeries(series);
                fillIsoLineSeries(series, CyCoefficient, Xm, windSpeedMaxGroundLevelConcentrationDistance, harmfulSubstancesDepositionCoefficient, groundLevelConcentration,
                        windSpeed);
            }
        }

        XYSeries macSeries = new XYSeries("ПДК = " + mac, false);
        dataset.addSeries(macSeries);
        fillIsoLineSeries(macSeries, mac, Xm, windSpeedMaxGroundLevelConcentrationDistance, harmfulSubstancesDepositionCoefficient, groundLevelConcentration,
                windSpeed);


        int x = Xm; //countX0(0, Xm, windSpeedMaxGroundLevelConcentrationDistance, harmfulSubstancesDepositionCoefficient, 0.1);
        double C1 = groundLevelConcentration * countS1(x, windSpeedMaxGroundLevelConcentrationDistance, harmfulSubstancesDepositionCoefficient);
        EquationFunction f = new QuarticFunction(45.1, 17, 12.8, 5, 1 - Math.sqrt(C1 / backgroundConcentration));
        for (double t : f.findRealRoots()) {
            if (t > 0) {
                XYSeries series = new XYSeries("Граничные линии", false);
                dataset.addSeries(series);
                double y = Math.sqrt(windSpeed > 5 ? t * x * x / 5 : t * x * x / windSpeed);
                series.add(x, y);
                series.add(0.0, 0.0);
                series.add(x, -y);
                break;
            }
        }

        return dataset;
    }

    private void fillIsoLineSeries(XYSeries series, double CyCoefficient, int Xm, double windSpeedMaxGroundLevelConcentrationDistance,
                                   double harmfulSubstancesDepositionCoefficient, double groundLevelConcentration, double windSpeed) {
            double C = CyCoefficient * groundLevelConcentration;

            // Находим граничные точки
            int x1 = countX0(0, Xm, windSpeedMaxGroundLevelConcentrationDistance, harmfulSubstancesDepositionCoefficient, CyCoefficient);
            int xN = countX0(Xm, MAX_DISTANCE, windSpeedMaxGroundLevelConcentrationDistance, harmfulSubstancesDepositionCoefficient, CyCoefficient);

            series.add(xN, 0);
            Map<Integer, Double> points = new TreeMap<>();
            int step = xN - x1 < 2000 ? SMALL_SERIES_STEP : BIG_SERIES_STEP;
            for (int x = xN - 1; x > x1; x-=step) {
                double C1 = countS1(x, windSpeedMaxGroundLevelConcentrationDistance, harmfulSubstancesDepositionCoefficient) * groundLevelConcentration;
                EquationFunction f = new QuarticFunction(45.1, 17, 12.8, 5, 1 - Math.sqrt(C1 / C));
                for (double t : f.findRealRoots()) {
                    if (t > 0) {
                        double y = Math.sqrt(windSpeed > 5 ? t * x * x / 5 : t * x * x / windSpeed);
                        points.put(x, y);
                        series.add(x, y);
                    }
                }
            }

            series.add(x1, 0);

            for (Map.Entry<Integer, Double> point : points.entrySet()) {
                series.add((double) point.getKey(), -point.getValue());
            }
            series.add(xN, 0);
    }

    private int countX0(int fromDistance, int toDistance, double windSpeedMaxGroundLevelConcentrationDistance,
                        double harmfulSubstancesDepositionCoefficient, double CyCoefficient) {
        for (int x = fromDistance; x < toDistance; x++) {
            double s1 = countS1(x, windSpeedMaxGroundLevelConcentrationDistance, harmfulSubstancesDepositionCoefficient);
            if (MathUtils.checkEquals(s1, CyCoefficient)) {
                return x;
            }
        }
        throw new IllegalArgumentException();
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

    private JFreeChart createSplineChart(XYDataset dataSet, Locale locale) {
        // Create plot
        NumberAxis xAxis = new NumberAxis(messageSource.getMessage("lab3.isoline-x-axis", null, locale));
        NumberAxis yAxis = new NumberAxis(messageSource.getMessage("lab3.isoline-y-axis", null, locale));
        XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
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

        // Draw png
        /*BufferedImage bi = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_BGR);
        Graphics graphics = bi.getGraphics();
        chartPanel.setBounds(0, 0, width, height);
        chartPanel.paint(graphics);
        ImageIO.write(bi, "png", new File(filename));*/
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
