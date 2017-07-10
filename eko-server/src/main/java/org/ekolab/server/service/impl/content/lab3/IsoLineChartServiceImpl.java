package org.ekolab.server.service.impl.content.lab3;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.ekolab.server.dev.LogExecutionTime;
import org.ekolab.server.model.content.lab3.Lab3Data;
import org.ekolab.server.service.api.content.lab3.IsoLineChartService;
import org.ekolab.server.service.impl.content.equations.ferrari.EquationFunction;
import org.ekolab.server.service.impl.content.equations.ferrari.QuarticFunction;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
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
import java.util.List;

import static java.lang.Math.pow;

/**
 * Created by 777Al on 24.04.2017.
 */
@Service
public class IsoLineChartServiceImpl implements IsoLineChartService {
    private static final double EPS = 0.0001;
    private static final int GRID_CELL_COUNT = 100000;
    private static final int STEP_INCREASE_SIZE = 100;
    private static final int MAX_POINTS_COUNT = 1;

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
        return createLineChart(getDataSet(data), data.getBwdMaxGroundLevelConcentrationDistance(), locale);
    }

    private XYDataset getDataSet(Lab3Data lab3Data) {
        Double Cm = lab3Data.getBwdNo2GroundLevelConcentration();
        Double Xm = lab3Data.getBwdMaxGroundLevelConcentrationDistance();
        Double CBackground = lab3Data.getNo2BackgroundConcentration();

        XYSeriesCollection dataset = new XYSeriesCollection();

        Double windSpeedMaxGroundLevelConcentrationDistance = lab3Data.getWindSpeedMaxGroundLevelConcentrationDistance();
        Double harmfulSubstancesDepositionCoefficient = lab3Data.getHarmfulSubstancesDepositionCoefficient();
        Double windSpeed = lab3Data.getWindSpeed();
        Double[] key = new Double[]{windSpeedMaxGroundLevelConcentrationDistance,
                harmfulSubstancesDepositionCoefficient, windSpeed};


        if (windSpeedMaxGroundLevelConcentrationDistance != null
                && harmfulSubstancesDepositionCoefficient != null && windSpeed != null) {
            windSpeed = windSpeed <= 5.0 ? windSpeed : 5.0;
            // Граничные прямые
            /*double a = CBackground / Cm;
            // Граничные прямые
            for (int x = 1; x < 100000; x++) {
                double s1 = countS1(x, windSpeedMaxGroundLevelConcentrationDistance,
                        harmfulSubstancesDepositionCoefficient);
                if (Math.abs(s1 - a) < EPS) {
                    // Нашли x, пора считать игрек
                    for (int y = 1; y < 100000; y++) {
                        double s2 = countS2(windSpeed, x, y);
                        if (Math.abs(s2 - 1) < EPS) {
                            XYSeries series = new XYSeries("Граничные линии", false);
                            series.add(x, y);
                            series.add(0, 0);
                            series.add(x, -y);
                            dataset.addSeries(series);

                            break;
                        }
                    }
                    break;
                }
            }*/

            //for (double CyCoefficient = 0.1; CyCoefficient < 1.0; CyCoefficient += 0.1) {
            boolean stop = false;
            for (double CyCoefficient = 0.5; CyCoefficient < 1.0; CyCoefficient += 0.1) {
                //XYSeries series = new XYSeries("Cm = " + CyCoefficient, false);
                //dataset.addSeries(series);

                List<Pair<Integer, Integer>> points = new ArrayList<>(MAX_POINTS_COUNT * 5);

                for (int x = Math.toIntExact(Math.round(Xm)); x < 100000 && !stop/*series.getItemCount() < MAX_POINTS_COUNT*/; x++) {
                    double s1 = countS1(x, windSpeedMaxGroundLevelConcentrationDistance,
                            harmfulSubstancesDepositionCoefficient);
                    double Cx = s1 * Cm;

                    if (Cx < CBackground || Cx > Cm) {
                        break;
                    }

                    for (int y1 = 0; y1 < 100000; y1++) {
                        double s2 = countS2(windSpeed, x, y1);

                        double Cy = s2 * Cx;

                        if (Cy < CBackground || Cy > Cm) {
                            break;
                        }

                        if (Math.abs(Cy - CyCoefficient * Cm) > EPS) {
                            continue;
                        }

                        double s1New = Cy / Cm;

                        //7)	Су(х1,у1) подставляется вместо Сх в уравнение (1)
                        for (int x2 = Math.toIntExact(Math.round(Xm)); x2 < GRID_CELL_COUNT && !stop/*series.getItemCount() < MAX_POINTS_COUNT*/; x2++) {
                            double Cx2 = Cm * countS1(x2, windSpeedMaxGroundLevelConcentrationDistance, harmfulSubstancesDepositionCoefficient);
                            if (Cx2 < CBackground || Cx2 > Cm) {
                                break;
                            }
                            if (Math.abs(Cx2 - Cy) < EPS) {
                                // 9)	Для вычисления точки на изолинии с концентрацией Су(х2;у1)
                                // необходимо в уравнение (1) подставить Сх=Су(х2;у1)
                                for (int x3 = Math.toIntExact(Math.round(Xm)); x3 < 100000; x3++) {
                                    if (Math.abs(s1New - countS1(x3, windSpeedMaxGroundLevelConcentrationDistance,
                                            harmfulSubstancesDepositionCoefficient)) < EPS) {
                                        // 11)	В уравнение (1) подставляется Сх=Су(х2;у1), где s1 для условия x<Xм
                                        for (int x4 = 0; x4 < Math.toIntExact(Math.round(Xm)); x4++) {
                                            if (Math.abs(s1New - countS1(x4, windSpeedMaxGroundLevelConcentrationDistance,
                                                    harmfulSubstancesDepositionCoefficient)) < EPS) {
                                                int iteration = points.size() / 5;
                                               /* points.add(iteration + 1, Pair.of(x4, 0));
                                                points.add(iteration + 2, Pair.of(x2, y1));
                                                points.add(iteration  + 3, Pair.of(x3, 0));
                                                points.add(iteration  + 4, Pair.of(x2, -y1));
                                                points.add(iteration + 1, Pair.of(x4, 0));//Замыкаю*/


                                                XYSeries series1 = new XYSeries("Cm1 = " + CyCoefficient, false);
                                                dataset.addSeries(series1);
                                                series1.add(x4, 0);
                                                series1.add(x2, y1);
                                                series1.add(x3, 0);
                                                stop=true;

                                                if (x2 + STEP_INCREASE_SIZE < GRID_CELL_COUNT) {
                                                    x2 += STEP_INCREASE_SIZE;
                                                }

                                                break;
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                /*for (Pair<Integer, Integer> point : points) {
                    series.add(point.getLeft(), point.getRight());
                }*/
            }
        }

        return dataset;
    }

    private double countS2(double windSpeed, double x, double y) {
        double t = windSpeed * y * y / (x * x);
        return 1.0 / Math.pow(1 + 5 * t + 12.8 * t * t + 17 * t * t * t + 45.1 * t * t * t * t, 2);
    }

    /*private XYDataset getDataSet(Lab3Data lab3Data) {
        Double windSpeedMaxGroundLevelConcentrationDistance = lab3Data.getWindSpeedMaxGroundLevelConcentrationDistance();
        Double harmfulSubstancesDepositionCoefficient = lab3Data.getHarmfulSubstancesDepositionCoefficient();
        Double windSpeed = lab3Data.getWindSpeed();
        Double[] key = new Double[]{windSpeedMaxGroundLevelConcentrationDistance,
                harmfulSubstancesDepositionCoefficient, windSpeed};
        return DATASET_CACHE.computeIfAbsent(key, k -> createDataset(windSpeedMaxGroundLevelConcentrationDistance,
                harmfulSubstancesDepositionCoefficient, windSpeed));
    }*/

    private XYDataset createDataset(Double windSpeedMaxGroundLevelConcentrationDistance,
                                      Double harmfulSubstancesDepositionCoefficient, Double windSpeed) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("isolineChart");

        dataset.addSeries(series);
        if (windSpeedMaxGroundLevelConcentrationDistance != null
                && harmfulSubstancesDepositionCoefficient != null  && windSpeed != null) {
            for (int x = -1000; x < 1000; x++) {
                for (double y : countY(x, windSpeedMaxGroundLevelConcentrationDistance,
                        harmfulSubstancesDepositionCoefficient, windSpeed)) {
                    series.add(x, y);
                    LOGGER.info("x = " + x + " y = " + y + " d = " + (double) x / windSpeedMaxGroundLevelConcentrationDistance);
                }
            }
        }

        return dataset;
    }

    /*private XYDataset createDataset(Double windSpeedMaxGroundLevelConcentrationDistance,
                                      Double harmfulSubstancesDepositionCoefficient, Double windSpeed) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("isolineChart");

        dataset.addSeries(series);
        if (windSpeedMaxGroundLevelConcentrationDistance != null
                && harmfulSubstancesDepositionCoefficient != null  && windSpeed != null) {
            for (int x = -1000; x < 1000; x++) {
                for (double y : countY(x, windSpeedMaxGroundLevelConcentrationDistance,
                        harmfulSubstancesDepositionCoefficient, windSpeed)) {
                    series.add(x, y);
                    LOGGER.info("x = " + x + " y = " + y + " d = " + (double) x / windSpeedMaxGroundLevelConcentrationDistance);
                }
            }
        }

        return dataset;
    }*/

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
        LOGGER.info(" s1 = " + s1);
       /* EquationFunction f2 = new QuarticFunction(-45.1, -17, -12.8, -5,  Math.sqrt(1 / s1) - 1);
        double[] result = ArrayUtils.addAll(f1.findRealRoots(), f2.findRealRoots());*/
        return Arrays.stream(f.findRealRoots()).filter(t -> t > 0).toArray();
    }

    private double[] countY(double x, double windSpeedMaxGroundLevelConcentrationDistance,
                            double harmfulSubstancesDepositionCoefficient, double windSpeed) {
        return countY(x, countS1(x, windSpeedMaxGroundLevelConcentrationDistance,
                harmfulSubstancesDepositionCoefficient), windSpeed);
    }

    private double[] countY(double x, double s1, double windSpeed) {
        LOGGER.info(" s1 = " + s1);

        double[] result = new double[0];
        for (double t : countT(s1)) {
            LOGGER.info(" t = " + t);
            double root = Math.sqrt(windSpeed > 5 ? t * x * x / 5 : t * x * x / windSpeed);
            result = ArrayUtils.addAll(result, root, -root);
        }
        return result;
    }

    private JFreeChart createLineChart(XYDataset dataSet, double startPoint, Locale locale) {
        NumberAxis xAxis = new NumberAxis(messageSource.getMessage("lab3.isoline-x-axis", null, locale));
        NumberAxis yAxis = new NumberAxis(messageSource.getMessage("lab3.isoline-y-axis", null, locale));

        XYSplineRenderer renderer = new XYSplineRenderer(2);
        XYPlot plot = new XYPlot(dataSet, xAxis, yAxis, renderer);
        plot.setBackgroundPaint(null);
        plot.getRangeAxis().setRange(-1000.0, 1000.0);
        //plot.setBackgroundImage(createStationInCityImage("map/moscow.svg", "map/moscow.svg"));
        //plot.setBackgroundImage(createStationInCityImage("map/moscow-min.png", "map/moscow-min.png"));
        try {
            plot.setBackgroundImage(ImageIO.read(IsoLineChartServiceImpl.class.getResourceAsStream("map/moscow-min1.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        plot.setAxisOffset(new RectangleInsets(4, 4, 4, 4));

        Marker updateMarker = new ValueMarker(startPoint);
        updateMarker.setPaint(Color.BLACK);
        XYTextAnnotation updateLabel = new XYTextAnnotation("Xm", startPoint, 20);
        updateLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        updateLabel.setRotationAnchor(TextAnchor.BASELINE_CENTER);
        updateLabel.setTextAnchor(TextAnchor.BASELINE_CENTER);
        updateLabel.setRotationAngle(-Math.PI / 2.0);
        updateLabel.setPaint(Color.BLACK);
        plot.addDomainMarker(updateMarker, Layer.BACKGROUND);
        plot.addAnnotation(updateLabel);

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
