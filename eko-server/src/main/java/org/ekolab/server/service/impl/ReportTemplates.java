package org.ekolab.server.service.impl;

import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizerBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;

import java.awt.*;
import java.util.Locale;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.tableOfContentsCustomizer;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;

public class ReportTemplates {
    public static final StyleBuilder rootStyle = stl.style().setFontName("DejaVu Serif").setPadding(2);
    public static final StyleBuilder boldStyle = stl.style(rootStyle).bold();
    public static final StyleBuilder italicStyle = stl.style(rootStyle).italic();
    public static final StyleBuilder boldCenteredStyle = stl.style(boldStyle).setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE);
    public static final StyleBuilder bold12CenteredStyle = stl.style(boldCenteredStyle).setFontSize(12);
    public static final StyleBuilder bold18CenteredStyle = stl.style(boldCenteredStyle).setFontSize(18);
    public static final StyleBuilder bold22CenteredStyle = stl.style(boldCenteredStyle).setFontSize(22);
    public static final StyleBuilder columnStyle = stl.style(rootStyle).setVerticalTextAlignment(VerticalTextAlignment.MIDDLE);
    public static final StyleBuilder columnTitleStyle = stl.style(columnStyle)
            .setBorder(stl.pen1Point())
            .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
            .setBackgroundColor(Color.LIGHT_GRAY)
            .bold();
    public static final StyleBuilder groupStyle = stl.style(boldStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);
    public static final StyleBuilder subtotalStyle = stl.style(boldStyle).setTopBorder(stl.pen1Point());

    public static final StyleBuilder crosstabGroupStyle = stl.style(columnTitleStyle);
    public static final StyleBuilder crosstabGroupTotalStyle = stl.style(columnTitleStyle)
            .setBackgroundColor(new Color(170, 170, 170));
    public static final StyleBuilder crosstabGrandTotalStyle = stl.style(columnTitleStyle)
            .setBackgroundColor(new Color(140, 140, 140));
    public static final StyleBuilder crosstabCellStyle = stl.style(columnStyle)
            .setBorder(stl.pen1Point());

    public static final TableOfContentsCustomizerBuilder tableOfContentsCustomizer = tableOfContentsCustomizer()
            .setHeadingStyle(0, stl.style(rootStyle).bold());

    public static ReportTemplateBuilder reportTemplate(Locale locale) {
        return template()
                .setLocale(locale)
                .setColumnStyle(columnStyle)
                .setColumnTitleStyle(columnTitleStyle)
                .setGroupStyle(groupStyle)
                .setGroupTitleStyle(groupStyle)
                .setSubtotalStyle(subtotalStyle)
                .highlightDetailEvenRows()
                .crosstabHighlightEvenRows()
                .setCrosstabGroupStyle(crosstabGroupStyle)
                .setCrosstabGroupTotalStyle(crosstabGroupTotalStyle)
                .setCrosstabGrandTotalStyle(crosstabGrandTotalStyle)
                .setCrosstabCellStyle(crosstabCellStyle)
                .setTableOfContentsCustomizer(tableOfContentsCustomizer);
    }

    public static ComponentBuilder<?, ?> createTitleComponent(String label) {
        return cmp.horizontalList()
                .add(cmp.text(label).setStyle(bold18CenteredStyle).setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT))
                .newRow()
                .add(cmp.line())
                .newRow()
                .add(cmp.verticalGap(10));
    }
}