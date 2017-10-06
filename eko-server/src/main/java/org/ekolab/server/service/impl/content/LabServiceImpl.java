package org.ekolab.server.service.impl.content;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.Language;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.apache.commons.lang.UnhandledException;
import org.ekolab.server.common.MathUtils;
import org.ekolab.server.common.UserInfoUtils;
import org.ekolab.server.dao.api.content.LabDao;
import org.ekolab.server.dev.LogExecutionTime;
import org.ekolab.server.model.DomainModel;
import org.ekolab.server.model.UserInfo;
import org.ekolab.server.model.content.Calculated;
import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.LabTest;
import org.ekolab.server.model.content.LabTestHomeWorkQuestion;
import org.ekolab.server.model.content.LabTestQuestionVariant;
import org.ekolab.server.model.content.LabTestQuestionVariantWithAnswers;
import org.ekolab.server.model.content.LabVariant;
import org.ekolab.server.model.content.Validated;
import org.ekolab.server.model.content.lab3.Valued;
import org.ekolab.server.service.api.ReportService;
import org.ekolab.server.service.api.UserInfoService;
import org.ekolab.server.service.api.content.LabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
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
    protected UserInfoService userInfoService;

    @Autowired
    protected ReportService reportService;

    @Autowired
    protected MessageSource messageSource;

    protected final LabDao<T> labDao;

    protected LabServiceImpl(LabDao<T> labDao) {
        this.labDao = labDao;
    }

    @Override
    public boolean isFieldValidated(Field field) {
        return field.getAnnotation(Validated.class) != null;
    }

    /**
     * Проверяет правильность значения поля
     * @param field поле
     * @param value значение
     * @param labData данные лабораторной
     * @return признак того, что значение верно
     */
    @Override
    public boolean validateFieldValue(Field field, Object value, T labData) {
        return AnnotationUtils.findAnnotation(field, Validated.class) == null ||
                validateFieldValue(field.getName(), value, labData);
    }

    @Override
    public boolean isFieldCalculated(Field field) {
        return field.getAnnotation(Calculated.class) != null;
    }

    @Override
    @Transactional(readOnly = true)
    //@Cacheable(cacheNames = "LABDATA", key = "#userName")
    public T getLastUncompletedLabByUser(String userName) {
        return getLastLabByUser(userName, false);
    }

    @Override
    public T getCompletedLabByUser(String userName) {
        return getLastLabByUser(userName, true);
    }

    @Override
    @Transactional(readOnly = true)
    //@Cacheable(cacheNames = "LABDATA", key = "#userName")
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
    //@CachePut(cacheNames = "LABDATA", key = "#userName")
    public T startNewLab(String userName) {
        T labData = createBaseLabData(userName);
        labData.setVariant(generateNewLabVariant());
        labDao.saveLab(labData);
        return labData;
    }

    @Override
    @Transactional
    //@CachePut(cacheNames = "LABDATA", key = "#labData.userLogin")
    public T updateLab(T labData) {
        labData.setSaveDate(LocalDateTime.now());
        labDao.updateLab(labData);
        return updateCalculatedFields(labData);
    }

    @Override
    @Transactional
    //@CacheEvict(cacheNames = "LABDATA", key = "#userName")
    public int removeLabsByUser(String userName) {
        return labDao.removeLabsByUser(userName);
    }

    @Override
    @Transactional
    //@CacheEvict(cacheNames = "LABDATA")
    public int removeOldLabs(LocalDateTime lastSaveDate) {
        return labDao.removeOldLabs(lastSaveDate);
    }

    /**
     * Возвращает тестовые вопросы.
     * Ищем их в файлах ресурсов по маске: "lab-<lab-number>-test-question-<question-variant>-<question-number>"
     * Название вопроса: "lab-<lab-number>-test-question-<question-variant>-<question-number>"
     * Варианты ответа по: "lab-<lab-number>-test-question-<question-variant>-<question-number>-variant-<variant-number>"
     * Правильным вариантом является: "lab-<lab-number>-test-question-<question-variant>-<question-number>-variant-<variant-number>-right"
     * @return тестовые вопросы
     */
    @Override
    @Cacheable("LAB_TEST")
    public LabTest getLabTest(Locale locale) {
        LabTest test = new LabTest();
        test.setQuestions(labDao.getTestQuestions(locale));
        return test;
    }

    @Override
    public int checkLabTest(LabData<?> data, Map<LabTestQuestionVariant, Object> answers) {
        int errors = 0;
        Bindings values = new SimpleBindings(getValuesFromModel(data));

        for (Map.Entry<LabTestQuestionVariant, Object> entry : answers.entrySet()) {
            if (entry.getKey() instanceof LabTestQuestionVariantWithAnswers) {
                LabTestQuestionVariantWithAnswers variant = (LabTestQuestionVariantWithAnswers) entry.getKey();
                if (!variant.getAnswers().get(variant.getRightAnswer() - 1).equals(entry.getValue())) {
                    errors++;
                }
            } else {
                LabTestHomeWorkQuestion question = (LabTestHomeWorkQuestion) entry.getKey();
                ScriptEngineManager mgr = new ScriptEngineManager();
                ScriptEngine engine = mgr.getEngineByName(Language.JAVA_SCRIPT);
                try {
                    Object value = engine.eval(question.getFormulae(), values);
                    if (Number.class.isAssignableFrom(question.getValueType())) {
                        if (!MathUtils.checkEquals(((Number) entry.getValue()).doubleValue(), ((Number) value).doubleValue())) {
                            errors++;
                        }
                    } else if (question.getValueType() == Boolean.class) {
                        if (!entry.getValue().equals(value)) {
                            errors++;
                        }
                    }
                } catch (ScriptException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }
        return errors;
    }

    @Override
    @Cacheable("COMPLETED_TEST")
    public boolean isTestCompleted(String userName) {
        return labDao.isTestCompleted(userName);
    }

    @Override
    @CacheEvict("COMPLETED_TEST")
    public void setTestCompleted(String userName) {
        labDao.setTestCompleted(userName);
    }

    @LogExecutionTime(500)
    @Override
    public byte[] printInitialData(LabVariant variant, Locale locale) {
        TextColumnBuilder<String> parameterNameColumn = col.column(messageSource.
                getMessage("report.lab-data.parameter-name", null, locale), "parameterName", type.stringType());
        TextColumnBuilder<String> parameterValueColumn = col.column(messageSource.
                getMessage("report.lab-data.parameter-value", null, locale), "parameterValue", type.stringType())
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
        return reportService.printReport(report()
                .setTemplate(reportService.getReportTemplate(locale))
                .title(reportService.createTitleComponent(messageSource.getMessage("report.initial-data.title", new Object[]{getLabNumber()}, locale)))
                .columns(parameterNameColumn, parameterValueColumn)
                .setDataSource(createDataSourceFromModel(variant, locale)));
    }

    @Override
    public Map<String, String> getPrintData(DomainModel data, Locale locale) {
        Map<String, String> printData = new HashMap<>();
        for (Map.Entry<String, Object> entry : getValuesFromModel(data).entrySet()) {
            String name = messageSource.getMessage(entry.getKey(), null, locale);
            String value = getLocalizedFieldValue(entry.getValue(), locale);
            printData.put(name, value);
        }
        return printData;
    }

    protected String getLocalizedFieldValue(Object value, Locale locale) {
        return value.getClass().isEnum() ?
                messageSource.getMessage(value.getClass().getSimpleName()
                        + '.' + ((Enum<?>) value).name(), null, locale) : String.valueOf(value);
    }

    protected JRDataSource createDataSourceFromModel(DomainModel data, Locale locale) {
        DRDataSource dataSource = new DRDataSource("parameterName", "parameterValue");
        getPrintData(data, locale).forEach((key, value) -> dataSource.add(key, value));

        return dataSource;
    }

    protected Map<String, Object> getValuesForReport(T data, Locale locale) {
        Map<String, Object> values = new HashMap<>();
        getValuesFromModel(data).forEach((key, value) -> values.put(key,
                value.getClass().isEnum() || String.class.isAssignableFrom(value.getClass()) ?
                        getLocalizedFieldValue(value, locale) : value));
        return values;
    }

    protected Map<String, Object> getValuesFromModel(DomainModel data) {
        Map<String, Object> values = new HashMap<>();
        for (Field field : data.getClass().getDeclaredFields()) {
            ReflectionUtils.makeAccessible(field);
            Object value = ReflectionUtils.getField(field, data);
            if (value instanceof Valued) {
                values.put(field.getName(), ((Valued) value).value());
            } else if (value == null || value.getClass().isEnum()) {
                values.put(field.getName(), value);
            } else {
                values.put(field.getName(), ReflectionUtils.getField(field, data));
            }
        }
        return values;
    }


    /**
     * Возвращает печатный вариант отчёта в PDF формате.
     * todo На второй странице отчёта печатается график изолиний в вертикальной ориентации.
     *
     * @param labData данные лабораторной работы.
     * @param locale  язык.
     * @return печатный вариант данных в PDF формате.
     */
    @Override
    public byte[] createReport(T labData, Locale locale) {
        try {
            JasperReport report = reportService.getCompiledReport(this.getClass().getResource("report/report.jrxml"));
            JRDataSource dataSource = new JREmptyDataSource();
            JasperPrint print = JasperFillManager.fillReport(report, getValuesForReport(labData, locale), dataSource);
            return JasperExportManager.exportReportToPdf(print);
        } catch (JRException e) {
            throw new UnhandledException(e);
        }
    }

    @Deprecated
    protected JasperReportBuilder createBaseLabReport(LabData<?> labData, Locale locale) {
        UserInfo userInfo = userInfoService.getUserInfo(labData.getUserLogin());

        TextColumnBuilder<String> parameterNameColumn = col.column(messageSource.getMessage("report.lab-data.parameter-name", null, locale), "parameterName", type.stringType());
        TextColumnBuilder<String> parameterValueColumn = col.column(messageSource.getMessage("report.lab-data.parameter-value", null, locale), "parameterValue", type.stringType())
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
        return report()
                .setTemplate(reportService.getReportTemplate(locale))
                .title(reportService.createTitleComponent(messageSource.
                        getMessage("report.lab-data.title", new Object[]{getLabNumber(), UserInfoUtils.getShortInitials(userInfo)}, locale)))
                .columns(parameterNameColumn, parameterValueColumn)
                .setDataSource(createDataSourceFromModel(labData, locale));
    }

    protected T createBaseLabData(String userName) {
        T labData = createNewLabData();
        labData.setUserLogin(userName);
        labData.setStartDate(LocalDateTime.now());
        labData.setSaveDate(LocalDateTime.now());
        return labData;
    }

    protected T getLastLabByUser(String userName, boolean completed) {
        T data = labDao.getLastLabByUser(userName, completed);
        if (data != null) {
            data.setUserLogin(userName);
            updateCalculatedFields(data);
        }
        return data;
    }

    /**
     * Создаёт структуру с данными лабораторной, не сохраняя её
     * @return новая структура с данными лабораторной
     */
    protected abstract T createNewLabData();

    protected boolean validateFieldValue(String fieldName, Object value, T labData) {
        return true;
    }

    /**
     * Генерирует вариант лабораторной, не сохраняя его
     * @return вариант лабораторной
     */
    protected abstract V generateNewLabVariant();
}
