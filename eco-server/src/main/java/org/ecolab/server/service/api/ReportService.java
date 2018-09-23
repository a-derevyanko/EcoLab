package org.ecolab.server.service.api;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.component.VerticalListBuilder;
import net.sf.jasperreports.engine.JasperReport;

import java.awt.Image;
import java.net.URL;

public interface ReportService {

    VerticalListBuilder createImageWithTitle(Image image, String title);

    ReportTemplateBuilder getReportTemplate();

    ComponentBuilder<?, ?> createTitleComponent(String label);

    /**
     * Печатает отчёт в PDF.
     * @param reportBuilder принтформа отчёта.
     * @return отчёт в PDF формате.
     */
    byte[] printReport(JasperReportBuilder reportBuilder);

    /**
     * Возвращает скомпилированный отчёт без подстановки параметров
     * @param templateUrl путь к шаблону
     * @return скомпилированный отчёт
     */
    JasperReport getCompiledReport(URL templateUrl);
}