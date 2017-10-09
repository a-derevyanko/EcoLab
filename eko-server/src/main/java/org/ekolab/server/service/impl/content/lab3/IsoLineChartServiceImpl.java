package org.ekolab.server.service.impl.content.lab3;

import org.apache.commons.math3.util.Precision;
import org.ekolab.server.common.MathUtils;
import org.ekolab.server.dev.LogExecutionTime;
import org.ekolab.server.model.content.lab3.City;
import org.ekolab.server.model.content.lab3.FuelType;
import org.ekolab.server.model.content.lab3.Lab3Data;
import org.ekolab.server.service.api.content.LabChartType;
import org.ekolab.server.service.api.content.lab3.IsoLineChartService;
import org.ekolab.server.service.api.content.lab3.Lab3ChartType;
import org.ekolab.server.service.api.content.lab3.Lab3ResourceService;
import org.ekolab.server.service.impl.content.equations.ferrari.EquationFunction;
import org.ekolab.server.service.impl.content.equations.ferrari.QuarticFunction;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYDrawableAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.LineBorder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.CompositeTitle;
import org.jfree.chart.title.ImageTitle;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.title.Title;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
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
    private static final int BIG_SERIES_STEP = 100;
    private static final int SMALL_SERIES_STEP = 10;
    private static final Color EKO_LAB_COLOR = new Color(143, 184, 43);
    private static final Logger LOGGER = LoggerFactory.getLogger(IsoLineChartServiceImpl.class);
    private static final Map<Double[], XYSeriesCollection> DATASET_CACHE = new WeakHashMap<>();

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private Lab3ResourceService resourceService;

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
            String chartTitle;
            if (chartType == Lab3ChartType.ISOLINE) {
                groundLevelConcentration = labData.getBwdNoxGroundLevelConcentration();
                backgroundConcentration = labData.getNo2BackgroundConcentration();
                mac = labData.getNo2MAC();
                chartTitle = messageSource.getMessage("lab3.isoline-chart-title-nox", null, locale);
            } else if (chartType == Lab3ChartType.SO2) {
                groundLevelConcentration = labData.getBwdSo2GroundLevelConcentration();
                backgroundConcentration = labData.getSo2BackgroundConcentration();
                mac = labData.getSo2MAC();
                chartTitle = messageSource.getMessage("lab3.isoline-chart-title-so2", null, locale);
            } else if (chartType == Lab3ChartType.ASH) {
                groundLevelConcentration = labData.getBwdAshGroundLevelConcentration();
                backgroundConcentration = labData.getAshBackgroundConcentration();
                mac = labData.getAshMAC();
                chartTitle = messageSource.getMessage("lab3.isoline-chart-title-ash", null, locale);
            } else {
                throw new IllegalArgumentException("Unknown chart type");
            }

            //todo пока проверки прибиты - нужно вынести их в валидацию полей
            if (mac != null && mac > 1) {
                return null;
            }
            if (groundLevelConcentration != null && backgroundConcentration != null && mac != null) {
                return createSplineChart(labData, chartTitle, getDataSet(windSpeedMaxGroundLevelConcentrationDistance,
                        harmfulSubstancesDepositionCoefficient, groundLevelConcentration,
                        backgroundConcentration, windSpeed, mac, locale), windSpeedMaxGroundLevelConcentrationDistance, groundLevelConcentration, locale);
            }
        }
        return null;
    }

    private XYSeriesCollection getDataSet(double windSpeedMaxGroundLevelConcentrationDistance,
                                 double harmfulSubstancesDepositionCoefficient, double groundLevelConcentration,
                                 double no2BackgroundConcentration, double windSpeed, double mac, Locale locale) {

        Double[] key = new Double[]{windSpeedMaxGroundLevelConcentrationDistance,
                harmfulSubstancesDepositionCoefficient, groundLevelConcentration, no2BackgroundConcentration, windSpeed, mac};

        return DATASET_CACHE.computeIfAbsent(key, k -> createDataset(
                windSpeedMaxGroundLevelConcentrationDistance,
                harmfulSubstancesDepositionCoefficient, groundLevelConcentration, no2BackgroundConcentration, windSpeed, mac, locale));
    }

    private XYSeriesCollection createDataset(double windSpeedMaxGroundLevelConcentrationDistance,
                                    double harmfulSubstancesDepositionCoefficient, double groundLevelConcentration,
                                    double backgroundConcentration, double windSpeed, double mac, Locale locale) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        int Xm = Math.toIntExact(Math.round(windSpeedMaxGroundLevelConcentrationDistance));

        for (double CyCoefficient = 0.1; CyCoefficient < 0.95; CyCoefficient += 0.1) {
            double C = CyCoefficient * groundLevelConcentration;
            if (C > backgroundConcentration) {
                String description = String.valueOf(Precision.round(CyCoefficient, 1));
                XYSeries series = new XYSeries("C = " + description +" Cm", false);
                series.setDescription(description);
                dataset.addSeries(series);
                fillIsoLineSeries(series, CyCoefficient, Xm, windSpeedMaxGroundLevelConcentrationDistance, harmfulSubstancesDepositionCoefficient, groundLevelConcentration,
                        windSpeed);
            }
        }

        String backgroundName = messageSource.getMessage("lab3.isoline-background-name", new Object[]{backgroundConcentration}, locale);
        XYSeries borderSeries = new XYSeries(backgroundName, false);
        borderSeries.setDescription(backgroundName);
        dataset.addSeries(borderSeries);
        double borderCyCoefficient = backgroundConcentration / groundLevelConcentration;
        fillIsoLineSeries(borderSeries, borderCyCoefficient, Xm, windSpeedMaxGroundLevelConcentrationDistance, harmfulSubstancesDepositionCoefficient, groundLevelConcentration,
                windSpeed);

        if (backgroundConcentration + groundLevelConcentration > mac) {
            XYSeries macSeries = new XYSeries(messageSource.getMessage("lab3.isoline-mac-name", new Object[]{mac}, locale), false);
            dataset.addSeries(macSeries);
            double macCyCoefficient = mac / groundLevelConcentration;
            fillIsoLineSeries(macSeries, macCyCoefficient, Xm, windSpeedMaxGroundLevelConcentrationDistance, harmfulSubstancesDepositionCoefficient, groundLevelConcentration,
                    windSpeed);
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

    private JFreeChart createSplineChart(Lab3Data labData,
                                         String chartName,
                                         XYSeriesCollection dataSet,
                                         double Xm,
                                         double Cm,
                                         Locale locale) {
        NumberAxis xAxis = new NumberAxis(messageSource.getMessage("lab3.isoline-x-axis", null, locale));
        NumberAxis yAxis = new NumberAxis(messageSource.getMessage("lab3.isoline-y-axis", null, locale));
        XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
        XYPlot plot = new XYPlot(dataSet, xAxis, yAxis, renderer);

        // Добавляем маркер с Xm
        final CircleDrawer cd = new CircleDrawer(Color.white, new BasicStroke(1.0f), Color.BLACK);
        final XYAnnotation CmPoint = new XYDrawableAnnotation(Xm, 0.0, 5.0, 5.0, cd);
        plot.addAnnotation(CmPoint);
        final XYTextAnnotation xMMarker = new XYTextAnnotation("Cm", Xm - 70.0, 0.0);
        xMMarker.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
        xMMarker.setPaint(Color.BLUE);
        xMMarker.setTextAnchor(TextAnchor.HALF_ASCENT_RIGHT);
        plot.addAnnotation(xMMarker);

        List<XYSeries> seriesWithLabels = new ArrayList<>(dataSet.getSeries());

        boolean macSeriesExists = seriesWithLabels.removeIf(xySeries -> xySeries.getDescription() == null);

        for (XYSeries series : seriesWithLabels) {
            final XYTextAnnotation seriesNameMarker = new XYTextAnnotation(series.getDescription(), series.getX(0).doubleValue(), 0.0);
            seriesNameMarker.setBackgroundPaint(Color.WHITE);
            seriesNameMarker.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 2));
            seriesNameMarker.setPaint(Color.BLUE);
            seriesNameMarker.setTextAnchor(TextAnchor.HALF_ASCENT_CENTER);
            plot.addAnnotation(seriesNameMarker);
        }
        plot.setBackgroundImage(resourceService.getBackgroundImage(labData.getCity(), labData.getWindDirection()));
        plot.setAxisOffset(new RectangleInsets(4, 4, 4, 4));

        // Create chart
        JFreeChart chart = new JFreeChart(chartName,
                JFreeChart.DEFAULT_TITLE_FONT, plot, false);
        ImageTitle windRoseTitle = new ImageTitle(resourceService.getWindRoseImage(labData.getCity()), 150, 150,
                Title.DEFAULT_POSITION, Title.DEFAULT_HORIZONTAL_ALIGNMENT,
                Title.DEFAULT_VERTICAL_ALIGNMENT, Title.DEFAULT_PADDING);
        windRoseTitle.setPosition(RectangleEdge.LEFT);
        windRoseTitle.setPadding(10.0, 0.0, 20.0, 0.0);

        LegendTitle legendTitle = new LegendTitle(plot);
        legendTitle.setPosition(RectangleEdge.LEFT);

        ChartUtilities.applyCurrentTheme(chart);

        TextTitle textTitle = new TextTitle(messageSource.getMessage("lab3.isoline-text-legend",
                new Object[]{
                        messageSource.getMessage(City.class.getSimpleName() + '.' + labData.getCity().name(), null, locale),
                        labData.getTppOutput(),
                        messageSource.getMessage(FuelType.class.getSimpleName() + '.' + labData.getVariant().getFuelType(), null, locale),
                        Precision.round(Cm, 1),
                        Precision.round(Xm, 1)}, locale), new Font(Font.SANS_SERIF, Font.BOLD, 12));
        textTitle.setTextAlignment(HorizontalAlignment.LEFT);

        BlockContainer blockContainer = new BlockContainer();
        blockContainer.add(textTitle, RectangleEdge.TOP);
        blockContainer.add(windRoseTitle);
        blockContainer.add(legendTitle, RectangleEdge.BOTTOM);
        blockContainer.setPadding(5.0, 5.0, 5.0, 5.0);
        CompositeTitle title = new CompositeTitle(blockContainer);
        title.setPosition(RectangleEdge.LEFT);
        title.setMargin(new RectangleInsets(1.0, 1.0, 1.0, 1.0));
        title.setFrame(new LineBorder());
        chart.addSubtitle(title);

        // Все линии, кроме граничной и ПДК делаем серого цвета
        for (int i = 0; i < seriesWithLabels.size() - 1; i++) {
            renderer.setSeriesPaint(i, Color.BLACK);
        }

        renderer.setSeriesStroke(seriesWithLabels.size() - 1, new BasicStroke(2.0f));
        renderer.setSeriesPaint(seriesWithLabels.size() - 1, EKO_LAB_COLOR);
        if (macSeriesExists) {
            renderer.setSeriesStroke(dataSet.getSeriesCount() - 1, new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{2.0f}, 0));
        }

        return chart;

    }
}
