package org.ekolab.server.service.api;

import net.sf.jasperreports.engine.JasperReport;

import java.util.Map;

/**
 * Created by 777Al on 27.04.2017.
 */
public interface ReportsService {
    JasperReport compileReport(String templatePath);

    byte[] fillReport(JasperReport report, Map<String, Object> data);
}
