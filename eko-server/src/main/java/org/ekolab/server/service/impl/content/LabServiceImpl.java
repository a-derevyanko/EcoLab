package org.ekolab.server.service.impl.content;

import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import org.ekolab.server.dao.api.content.LabDao;
import org.ekolab.server.dev.LogExecutionTime;
import org.ekolab.server.model.content.Calculated;
import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.LabVariant;
import org.ekolab.server.service.api.ReportsService;
import org.ekolab.server.service.api.content.LabService;
import org.ekolab.server.service.impl.ReportTemplates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

/**
 * Created by 777Al on 26.04.2017.
 */
public abstract class LabServiceImpl<T extends LabData<V>, V extends LabVariant> implements LabService<T> {
    @Autowired
    protected MessageSource messageSource;

    @Autowired
    protected ReportsService reportsService;

    private final LabDao<T> labDao;

    protected LabServiceImpl(LabDao<T> labDao) {
        this.labDao = labDao;
    }

    @Override
    public boolean isFieldCalculated(Field field) {
        return field.getAnnotation(Calculated.class) != null;
    }

    @Override
    @Transactional(readOnly = true)
    public T getLastUncompletedLabByUser(String userName) {
        T data = labDao.getLastUncompletedLabByUser(userName);
        if (data != null) {
            data.setUserLogin(userName);
            updateCalculatedFields(data);
        }
        return data;
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> getAllLabsByUser(String userName) {
        List<T> labs = labDao.getAllLabsByUser(userName);
        labs.forEach(item -> {
            item.setUserLogin(userName);
            updateCalculatedFields(item);
        });
        return labs;
    }

    @Override
    @Transactional
    public T startNewLab(String userName) {
        T labData = createNewLabData();
        labData.setUserLogin(userName);
        labData.setStartDate(LocalDateTime.now());
        labData.setSaveDate(LocalDateTime.now());
        labData.setVariant(generateNewLabVariant());
        labDao.saveLab(labData);
        return labData;
    }

    @Override
    @Transactional
    public T updateLab(T labData) {
        labData.setSaveDate(LocalDateTime.now());
        labDao.updateLab(labData);
        return updateCalculatedFields(labData);
    }

    @Override
    @Transactional
    public int removeLabsByUser(String userName) {
        return labDao.removeLabsByUser(userName);
    }

    @Override
    @Transactional
    public int removeOldLabs(LocalDateTime lastSaveDate) {
        return labDao.removeOldLabs(lastSaveDate);
    }

    @Override
    @LogExecutionTime(500)
    public byte[] printInitialData(Map<String, String> values, Locale locale) {
        TextColumnBuilder<String> parameterNameColumn = col.column(messageSource.getMessage("report.initial-data.parameter-name", null, locale), "parameterName", type.stringType());
        TextColumnBuilder<String> parameterValueColumn = col.column(messageSource.getMessage("report.initial-data.parameter-value", null, locale), "parameterValue", type.stringType());
        try {
            return JasperExportManager.exportReportToPdf(report()
                    .setTemplate(ReportTemplates.reportTemplate(locale))
                    .columns(parameterNameColumn, parameterValueColumn)
                    .title(ReportTemplates.createTitleComponent(messageSource.getMessage("report.initial-data.title", null, locale)))
                    .setDataSource(createDataSource(values, locale))
                    .toJasperPrint());
        } catch (DRException | JRException e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected JRDataSource createDataSource(Map<String, String> values, Locale locale) {
        DRDataSource dataSource = new DRDataSource("parameterName", "parameterValue");
        for (Map.Entry<String, String> entry : values.entrySet()) {
            dataSource.add(entry.getKey(), entry.getValue());
        }
        return dataSource;
    }

    protected byte[] createReport(String templatePath, Map<String, Object> data) {
        return reportsService.fillReport(reportsService.compileReport(templatePath), data);
    }

    /**
     * Генерирует вариант лабораторной, не сохраняя его
     * @return вариант лабораторной
     */
    protected abstract V generateNewLabVariant();
}
