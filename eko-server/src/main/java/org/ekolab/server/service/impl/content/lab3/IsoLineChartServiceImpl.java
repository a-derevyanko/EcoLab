package org.ekolab.server.service.impl.content.lab3;

import com.twelvemonkeys.image.ImageUtil;
import org.ekolab.server.common.MathUtils;
import org.ekolab.server.dev.LogExecutionTime;
import org.ekolab.server.model.content.lab3.City;
import org.ekolab.server.model.content.lab3.Lab3Data;
import org.ekolab.server.model.content.lab3.WindDirection;
import org.ekolab.server.service.api.content.LabChartType;
import org.ekolab.server.service.api.content.lab3.IsoLineChartService;
import org.ekolab.server.service.api.content.lab3.Lab3ChartType;
import org.ekolab.server.service.impl.content.equations.ferrari.EquationFunction;
import org.ekolab.server.service.impl.content.equations.ferrari.QuarticFunction;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYDrawableAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.GridArrangement;
import org.jfree.chart.block.LineBorder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.CompositeTitle;
import org.jfree.chart.title.ImageTitle;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static java.lang.Math.pow;

/**
 * Created by 777Al on 24.04.2017.
 */
@Service
public class IsoLineChartServiceImpl implements IsoLineChartService {
    private static final int MAX_DISTANCE = 35000;
    private static final int BIG_SERIES_STEP = 100;
    private static final int SMALL_SERIES_STEP = 10;
    private static final Logger LOGGER = LoggerFactory.getLogger(IsoLineChartServiceImpl.class);
    private static final Map<Double[], XYDataset> DATASET_CACHE = new WeakHashMap<>();
    private static final Map<City, Image> WIND_ROSE_CACHE = new HashMap<>(City.values().length);
    private static final Map<WindDirection, Image> BACKGROUND_CACHE = new HashMap<>(WindDirection.values().length * City.values().length);

    @Autowired
    private MessageSource messageSource;

    @PostConstruct
    private void fillWindRoseCache() throws IOException {
        for (City city : City.values()) {
            Image i = ImageIO.read(IsoLineChartServiceImpl.class.getResourceAsStream("wind/" + city.name() + ".png"));
            WIND_ROSE_CACHE.put(city, ImageUtil.createScaled(i, 200, 200, Image.SCALE_FAST));

            // todo должны быть разные картинки для городов
            Image background = ImageIO.read(IsoLineChartServiceImpl.class.getResourceAsStream("map/moscow-min1.png"));

            for (WindDirection direction : WindDirection.values()) {
                BACKGROUND_CACHE.put(direction, ImageUtil.createRotated(background, direction.ordinal() * (Math.PI / 0.25)));
            }
        }
    }

    @Override
    @LogExecutionTime(500)
    public JFreeChart createIsoLineChart(Lab3Data labData, Locale locale, LabChartType chartType) {
        Double windSpeedMaxGroundLevelConcentrationDistance = labData.getWindSpeedMaxGroundLevelConcentrationDistance();
        Double harmfulSubstancesDepositionCoefficient = labData.getHarmfulSubstancesDepositionCoefficient();
        Double windSpeed = labData.getWindSpeed();
        if (windSpeedMaxGroundLevelConcentrationDistance != null && harmfulSubstancesDepositionCoefficient != null && windSpeed != null)
        {
            Double groundLevelConcentration;
            Double backgroundConcentration;
            Double mac;
            String substance;
            if (chartType == Lab3ChartType.ISOLINE) {
                groundLevelConcentration = labData.getBwdNo2GroundLevelConcentration();
                backgroundConcentration = labData.getNo2BackgroundConcentration();
                mac = labData.getNo2MAC();
                substance = "NOx";
            } else if (chartType == Lab3ChartType.SO2) {
                groundLevelConcentration = labData.getBwdSo2GroundLevelConcentration();
                backgroundConcentration = labData.getSo2BackgroundConcentration();
                mac = labData.getSo2MAC();
                substance = "SO2";
            } else if (chartType == Lab3ChartType.ASH) {
                groundLevelConcentration = labData.getBwdAshGroundLevelConcentration();
                backgroundConcentration = labData.getAshBackgroundConcentration();
                mac = labData.getAshMAC();
                substance = "Золы"; //todo
            } else {
                throw new IllegalArgumentException("Unknown chart type");
            }

            //todo пока проверки прибиты - нужно вынести их в валидацию полей
            if (mac != null && mac > 1) {
                return null;
            }
            if (groundLevelConcentration != null && backgroundConcentration != null && mac != null) {
                return createSplineChart(labData.getCity(), labData.getWindDirection(), substance, getDataSet(windSpeedMaxGroundLevelConcentrationDistance,
                        harmfulSubstancesDepositionCoefficient, groundLevelConcentration,
                        backgroundConcentration, windSpeed, mac), windSpeedMaxGroundLevelConcentrationDistance, locale);
            }
        }
        return null;
    }

    private XYDataset getDataSet(double windSpeedMaxGroundLevelConcentrationDistance,
                                 double harmfulSubstancesDepositionCoefficient, double groundLevelConcentration,
                                 double no2BackgroundConcentration, double windSpeed, double mac) {

        Double[] key = new Double[]{windSpeedMaxGroundLevelConcentrationDistance,
                harmfulSubstancesDepositionCoefficient, windSpeed,
                groundLevelConcentration, no2BackgroundConcentration, mac};

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

        XYSeries macSeries = new XYSeries("ПДК = " + mac + " мг/м3", false);
        dataset.addSeries(macSeries);
        double macCyCoefficient = mac / groundLevelConcentration;
        fillIsoLineSeries(macSeries, macCyCoefficient, Xm, windSpeedMaxGroundLevelConcentrationDistance, harmfulSubstancesDepositionCoefficient, groundLevelConcentration,
                windSpeed);

        XYSeries borderSeries = new XYSeries("Граничные линии", false);
        dataset.addSeries(borderSeries);
        double borderCyCoefficient = backgroundConcentration / groundLevelConcentration;
        fillIsoLineSeries(borderSeries, borderCyCoefficient, Xm, windSpeedMaxGroundLevelConcentrationDistance, harmfulSubstancesDepositionCoefficient, groundLevelConcentration,
                windSpeed);

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

    private JFreeChart createSplineChart(City city, WindDirection windDirection, String substance, XYDataset dataSet, double Xm, Locale locale) {
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

        // Добавляем маркер с Xm
        final CircleDrawer cd = new CircleDrawer(Color.white, new BasicStroke(1.0f), Color.BLACK);
        final XYAnnotation bestBid = new XYDrawableAnnotation(Xm, 0.0, 5.0, 5.0, cd);
        plot.addAnnotation(bestBid);
        final XYTextAnnotation xMMarker = new XYTextAnnotation("Cm", Xm - 70.0, 0.0);
        xMMarker.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        xMMarker.setPaint(Color.BLUE);
        xMMarker.setTextAnchor(TextAnchor.HALF_ASCENT_RIGHT);
        plot.addAnnotation(xMMarker);
        //plot.setBackgroundImage(createStationInCityImage("map/moscow.svg", "map/moscow.svg"));
        //plot.setBackgroundImage(createStationInCityImage("map/moscow-min.png", "map/moscow-min.png"));
        plot.setBackgroundImage(BACKGROUND_CACHE.get(windDirection));
        plot.setAxisOffset(new RectangleInsets(4, 4, 4, 4));

        // Create chart
        JFreeChart chart = new JFreeChart(messageSource.getMessage("lab3.isoline-chart-title" + ' ' + substance, null, locale),
                JFreeChart.DEFAULT_TITLE_FONT, plot, false);
        ImageTitle imageTitle = new ImageTitle(WIND_ROSE_CACHE.get(city));
        imageTitle.setPosition(RectangleEdge.LEFT);
        LegendTitle legendTitle = new LegendTitle(plot);
        legendTitle.setPosition(RectangleEdge.LEFT);

        BlockContainer blockContainer = new BlockContainer(new GridArrangement(2, 1));
        blockContainer.add(imageTitle);
        blockContainer.add(legendTitle);
        blockContainer.setWidth(200.0);
        CompositeTitle title = new CompositeTitle(blockContainer);
        title.setPosition(RectangleEdge.LEFT);
        title.setMargin(new RectangleInsets(1.0, 1.0, 1.0, 1.0));
        title.setFrame(new LineBorder());
        chart.addSubtitle(title);
        ChartUtilities.applyCurrentTheme(chart);
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
