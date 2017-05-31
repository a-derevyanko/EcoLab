package org.ekolab.server.service.impl;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizerBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.Markup;
import net.sf.dynamicreports.report.constant.Rotation;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;

import java.awt.*;
import java.util.Locale;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.tableOfContentsCustomizer;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;

public class ReportTemplates {
    public static final StyleBuilder ROOT_STYLE = stl.style().setFontName("DejaVu Serif").setPadding(2);
    public static final StyleBuilder BOLD_STYLE = stl.style(ROOT_STYLE).bold();
    public static final StyleBuilder ITALIC_STYLE = stl.style(ROOT_STYLE).italic();
    public static final StyleBuilder BOLD_CENTERED_STYLE = stl.style(BOLD_STYLE).setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE);
    public static final StyleBuilder BOLD_12_CENTERED_STYLE = stl.style(BOLD_CENTERED_STYLE).setFontSize(12);
    public static final StyleBuilder BOLD_18_CENTERED_STYLE = stl.style(BOLD_CENTERED_STYLE).setFontSize(18);
    public static final StyleBuilder BOLD_22_CENTERED_STYLE = stl.style(BOLD_CENTERED_STYLE).setFontSize(22);
    public static final StyleBuilder COLUMN_STYLE = stl.style(ROOT_STYLE).setVerticalTextAlignment(VerticalTextAlignment.MIDDLE).setMarkup(Markup.HTML);
    public static final StyleBuilder COLUMN_TITLE_STYLE = stl.style(COLUMN_STYLE)
            .setBorder(stl.pen1Point())
            .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
            .setBackgroundColor(Color.LIGHT_GRAY)
            .bold();
    public static final StyleBuilder GROUP_STYLE = stl.style(BOLD_STYLE).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);
    public static final StyleBuilder SUBTOTAL_STYLE = stl.style(BOLD_STYLE).setTopBorder(stl.pen1Point());

    public static final StyleBuilder CROSSTAB_GROUP_STYLE = stl.style(COLUMN_TITLE_STYLE);
    public static final StyleBuilder CROSSTAB_GROUP_TOTAL_STYLE = stl.style(COLUMN_TITLE_STYLE)
            .setBackgroundColor(new Color(170, 170, 170));
    public static final StyleBuilder CROSSTAB_GRAND_TOTAL_STYLE = stl.style(COLUMN_TITLE_STYLE)
            .setBackgroundColor(new Color(140, 140, 140));
    public static final StyleBuilder CROSSTAB_CELL_STYLE = stl.style(COLUMN_STYLE)
            .setBorder(stl.pen1Point());
    public static final StyleBuilder ROTATED_CENTERED_STYLE = stl.style(BOLD_22_CENTERED_STYLE)
            .setBorder(stl.pen1Point()).setRotation(Rotation.RIGHT);

    public static final TableOfContentsCustomizerBuilder TABLE_OF_CONTENTS_CUSTOMIZER = tableOfContentsCustomizer()
            .setHeadingStyle(0, stl.style(ROOT_STYLE).bold());

    public static ReportTemplateBuilder reportTemplate(Locale locale) {
        return template()
                .setLocale(locale)
                .setColumnStyle(COLUMN_STYLE)
                .setColumnTitleStyle(COLUMN_TITLE_STYLE)
                .setGroupStyle(GROUP_STYLE)
                .setGroupTitleStyle(GROUP_STYLE)
                .setSubtotalStyle(SUBTOTAL_STYLE)
                .highlightDetailEvenRows()
                .crosstabHighlightEvenRows()
                .setCrosstabGroupStyle(CROSSTAB_GROUP_STYLE)
                .setCrosstabGroupTotalStyle(CROSSTAB_GROUP_TOTAL_STYLE)
                .setCrosstabGrandTotalStyle(CROSSTAB_GRAND_TOTAL_STYLE)
                .setCrosstabCellStyle(CROSSTAB_CELL_STYLE)
                .setTableOfContentsCustomizer(TABLE_OF_CONTENTS_CUSTOMIZER);
    }

    public static ComponentBuilder<?, ?> createTitleComponent(String label) {
        return cmp.horizontalList()
                .add(cmp.text(label).setStyle(BOLD_18_CENTERED_STYLE).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT))
                .newRow()
                .add(cmp.line())
                .newRow()
                .add(cmp.verticalGap(10));
    }

    /**
     * Печатает отчёт в PDF.
     * @param reportBuilder принтформа отчёта.
     * @return отчёт в PDF формате.
     */
    public static byte[] printReport(JasperReportBuilder reportBuilder) {
        try {
            return JasperExportManager.exportReportToPdf(reportBuilder.toJasperPrint());
        } catch (DRException | JRException e) {
            throw new IllegalArgumentException(e);
        }
    }
}