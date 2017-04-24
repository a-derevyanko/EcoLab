package org.ekolab.client.vaadin.server.ui.view.content.lab_3;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.CompositeErrorMessage;
import com.vaadin.server.ErrorMessage;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import org.ekolab.client.vaadin.server.service.I18N;
import org.ekolab.client.vaadin.server.service.ResourceService;
import org.ekolab.client.vaadin.server.ui.common.LabWizardStep;
import org.ekolab.client.vaadin.server.ui.styles.EkoLabTheme;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addon.JFreeChartWrapper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by 777Al on 06.04.2017.
 */
@SpringComponent
@ViewScope
public class Step5 extends GridLayout implements LabWizardStep {
    // ----------------------------- Графические компоненты --------------------------------
    private final Button so2MapButton = new Button("SO2 map", VaadinIcons.MAP_MARKER);
    private final Button ashMapButton = new Button("Ash map", VaadinIcons.MAP_MARKER);
    private final Button zoomInButton = new Button(VaadinIcons.PLUS_CIRCLE_O);
    private final Button zoomOutButton = new Button(VaadinIcons.MINUS_CIRCLE_O);

    @Autowired
    private I18N i18N;

    @Autowired
    private ResourceService resourceService;

    @Override
    public void init() throws IOException {
        LabWizardStep.super.init();
        setSizeFull();
        setMargin(true);
        setRows(20);
        setColumns(20);
        so2MapButton.setSizeFull();
        ashMapButton.setSizeFull();

        so2MapButton.addStyleName(EkoLabTheme.BUTTON_PRIMARY);
        ashMapButton.addStyleName(EkoLabTheme.BUTTON_PRIMARY);
        zoomInButton.addStyleName(EkoLabTheme.BUTTON_PRIMARY);
        zoomOutButton.addStyleName(EkoLabTheme.BUTTON_PRIMARY);

        //addComponent(createBasicDemo(), 4, 4, 19, 9);
    }

    @Override
    public void placeAdditionalButtons(HorizontalLayout buttonsLayout) {
        buttonsLayout.addComponents(zoomInButton, zoomOutButton, so2MapButton, ashMapButton);
    }

    @Override
    public ErrorMessage getComponentError() {
        Set<ErrorMessage> messageList = Stream.of(super.getComponentError())
                .filter(Objects::nonNull).collect(Collectors.toSet());
        return messageList.isEmpty() ? null : new CompositeErrorMessage(messageList);
    }

    /**
     * Returns a sample dataset.
     *
     * @return The dataset.
     */
    private static CategoryDataset createDataset() {

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

    }

    /**
     * Creates a sample chart.
     *
     * @param dataset
     *            the dataset.
     *
     * @return The chart.
     */
    private JFreeChart createchart(CategoryDataset dataset) throws IOException {

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
                        ImageIO.read(resourceService.getFileFromServer("img/logo.svg")),
                        ImageIO.read(resourceService.getFileFromServer("img/circle.gif"))));
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

    public JFreeChartWrapper createBasicDemo() throws IOException {
        JFreeChart createchart = createchart(createDataset());
        return new JFreeChartWrapper(createchart);
    }

    private static java.awt.Image createStationInCityImage(final BufferedImage bg, BufferedImage fg) {
        Graphics2D g2d = bg.createGraphics();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        g2d.drawImage(fg, 0, 0, null);
        g2d.dispose();
        return bg;
    }
}
