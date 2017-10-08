package org.ekolab.server.service.impl;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizerBuilder;
import net.sf.dynamicreports.report.constant.HorizontalImageAlignment;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.Markup;
import net.sf.dynamicreports.report.constant.Rotation;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperReport;
import org.apache.commons.lang.UnhandledException;
import org.ekolab.server.service.api.ReportService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.tableOfContentsCustomizer;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;

@Service
public class ReportServiceImpl implements ReportService {
    private static final StyleBuilder ROOT_STYLE = stl.style().setFontName("DejaVu Serif").setPadding(2);
    private static final StyleBuilder BOLD_STYLE = stl.style(ROOT_STYLE).bold();
    private static final StyleBuilder ITALIC_STYLE = stl.style(ROOT_STYLE).italic();
    private static final StyleBuilder BOLD_CENTERED_STYLE = stl.style(BOLD_STYLE).setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE);
    private static final StyleBuilder BOLD_12_CENTERED_STYLE = stl.style(BOLD_CENTERED_STYLE).setFontSize(12);
    private static final StyleBuilder BOLD_18_CENTERED_STYLE = stl.style(BOLD_CENTERED_STYLE).setFontSize(18);
    private static final StyleBuilder BOLD_22_CENTERED_STYLE = stl.style(BOLD_CENTERED_STYLE).setFontSize(22);
    private static final StyleBuilder COLUMN_STYLE = stl.style(ROOT_STYLE).setVerticalTextAlignment(VerticalTextAlignment.MIDDLE).setMarkup(Markup.HTML);
    private static final StyleBuilder COLUMN_TITLE_STYLE = stl.style(COLUMN_STYLE)
            .setBorder(stl.pen1Point())
            .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
            .setBackgroundColor(Color.LIGHT_GRAY)
            .bold();
    private static final StyleBuilder GROUP_STYLE = stl.style(BOLD_STYLE).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);
    private static final StyleBuilder SUBTOTAL_STYLE = stl.style(BOLD_STYLE).setTopBorder(stl.pen1Point());

    private static final StyleBuilder CROSSTAB_GROUP_STYLE = stl.style(COLUMN_TITLE_STYLE);
    private static final StyleBuilder CROSSTAB_GROUP_TOTAL_STYLE = stl.style(COLUMN_TITLE_STYLE)
            .setBackgroundColor(new Color(170, 170, 170));
    private static final StyleBuilder CROSSTAB_GRAND_TOTAL_STYLE = stl.style(COLUMN_TITLE_STYLE)
            .setBackgroundColor(new Color(140, 140, 140));
    private static final StyleBuilder CROSSTAB_CELL_STYLE = stl.style(COLUMN_STYLE)
            .setBorder(stl.pen1Point());
    private static final StyleBuilder ROTATED_CENTERED_STYLE = stl.style(BOLD_22_CENTERED_STYLE)
            .setBorder(stl.pen1Point()).setRotation(Rotation.RIGHT);

    private static final TableOfContentsCustomizerBuilder TABLE_OF_CONTENTS_CUSTOMIZER = tableOfContentsCustomizer()
            .setHeadingStyle(0, stl.style(ROOT_STYLE).bold());

    @Override
    public VerticalListBuilder createImageWithTitle(Image image, String title) {
        return cmp.verticalList(cmp.text(title).setStyle(BOLD_12_CENTERED_STYLE), cmp.image(image).setStyle(stl.style().setHorizontalImageAlignment(HorizontalImageAlignment.CENTER)));
    }

    @Override
    public ReportTemplateBuilder getReportTemplate(Locale locale) {
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

    @Override
    public ComponentBuilder<?, ?> createTitleComponent(String label) {
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
    @Override
    public byte[] printReport(JasperReportBuilder reportBuilder) {
        try {
            return JasperExportManager.exportReportToPdf(reportBuilder.toJasperPrint());
        } catch (DRException | JRException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Возвращает скомпилированный отчёт без подстановки параметров
     * @param templateUrl путь к шаблону
     * @return скомпилированный отчёт
     */
    @Override
    @Cacheable("COMPILED_REPORTS")
    public JasperReport getCompiledReport(@NotNull URL templateUrl) {
        try (InputStream is = templateUrl.openStream()) {
            return JasperCompileManager.compileReport(is);
        } catch (JRException | IOException e) {
            throw new UnhandledException(e);
        }
    }
}