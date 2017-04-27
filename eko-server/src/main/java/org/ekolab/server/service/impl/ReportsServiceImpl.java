package org.ekolab.server.service.impl;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.ekolab.server.service.api.ReportsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by 777Al on 27.04.2017.
 */
@Service
public class ReportsServiceImpl implements ReportsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportsServiceImpl.class);

    /**
     * Загружает шаблон и компилирует его.
     * @param templatePath путь к файлу шаблона.
     * @return скомпилированный шаблон.
     */
    @Override
    @Cacheable("COMPILED_REPORTS")
    public JasperReport compileReport(String templatePath) {
        try {
            return JasperCompileManager.compileReport(templatePath);
        } catch (JRException e) {
            throw new IllegalArgumentException("Error in compiling the report.... ", e);
        }
    }

    @Override
    public byte[] fillReport(JasperReport report, Map<String, Object> data) {
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, data);
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
