package org.ekolab.server.service.impl.content;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.HorizontalListBuilder;
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
import net.sf.jasperreports.types.date.FixedTimestamp;
import org.apache.commons.lang.UnhandledException;
import org.apache.commons.math3.util.Precision;
import org.ekolab.server.common.I18NUtils;
import org.ekolab.server.common.MathUtils;
import org.ekolab.server.common.UserInfoUtils;
import org.ekolab.server.dao.api.content.LabDao;
import org.ekolab.server.dev.LogExecutionTime;
import org.ekolab.server.model.DomainModel;
import org.ekolab.server.model.IdentifiedDomainModel;
import org.ekolab.server.model.StudentInfo;
import org.ekolab.server.model.UserGroup;
import org.ekolab.server.model.UserInfo;
import org.ekolab.server.model.content.Calculated;
import org.ekolab.server.model.content.DataValue;
import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.LabTest;
import org.ekolab.server.model.content.LabTestHomeWorkQuestion;
import org.ekolab.server.model.content.LabTestQuestion;
import org.ekolab.server.model.content.LabTestQuestionVariant;
import org.ekolab.server.model.content.LabTestQuestionVariantWithAnswers;
import org.ekolab.server.model.content.LabTestResult;
import org.ekolab.server.model.content.LabVariant;
import org.ekolab.server.model.content.lab3.Valued;
import org.ekolab.server.service.api.ReportService;
import org.ekolab.server.service.api.StudentInfoService;
import org.ekolab.server.service.api.UserInfoService;
import org.ekolab.server.service.api.content.LabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import javax.imageio.ImageIO;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import java.awt.Image;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

/**
 * Created by 777Al on 26.04.2017.
 */
public abstract class LabServiceImpl<T extends LabData<V>, V extends LabVariant, D extends LabDao<T>> implements LabService<T, V> {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(FixedTimestamp.TIMESTAMP_PATTERN);

    protected final UserInfoService userInfoService;

    protected final ReportService reportService;

    protected final MessageSource messageSource;

    protected final StudentInfoService studentInfoService;

    protected final D labDao;

    @Autowired
    protected LabServiceImpl(D labDao, UserInfoService userInfoService, ReportService reportService, MessageSource messageSource, StudentInfoService studentInfoService) {
        this.labDao = labDao;
        this.userInfoService = userInfoService;
        this.reportService = reportService;
        this.messageSource = messageSource;
        this.studentInfoService = studentInfoService;
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
    @Transactional
    //@CachePut(cacheNames = "LABDATA", key = "#userName")
    public T startNewLab(String userName) {
        T labData = createBaseLabData(userName);
        labData.setVariant(generateNewLabVariant());
        labDao.saveLab(labData);
        updateCalculatedFields(labData);
        return labData;
    }

    @Override
    @Transactional
    //@CachePut(cacheNames = "LABDATA", key = "#labData.userLogin")
    public T updateLab(T labData) {
        labData.setSaveDate(LocalDateTime.now());
        labDao.updateLab(labData);
        updateCalculatedFields(labData);
        return labData;
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
    @Cacheable(value = "LAB_TEST", key = "#root.targetClass.simpleName")
    public LabTest getLabTest(Locale locale) {
        LabTest test = new LabTest();
        test.setQuestions(labDao.getTestQuestions(locale));
        return test;
    }

    @Override
    public LabTestResult checkLabTest(LabData<?> data, Map<LabTestQuestionVariant, Object> answers, Locale locale) {
        List<Integer> errors = new ArrayList<>();
        Bindings values = new SimpleBindings(getValuesFromModel(data));

        for (Map.Entry<LabTestQuestionVariant, Object> entry : answers.entrySet()) {
            if (entry.getKey() instanceof LabTestQuestionVariantWithAnswers) {
                LabTestQuestionVariantWithAnswers variant = (LabTestQuestionVariantWithAnswers) entry.getKey();
                if (!variant.getAnswers().get(variant.getRightAnswer() - 1).equals(entry.getValue())) {
                    errors.add(variant.getNumber());
                }
            } else {
                LabTestHomeWorkQuestion question = (LabTestHomeWorkQuestion) entry.getKey();
                ScriptEngineManager mgr = new ScriptEngineManager();
                ScriptEngine engine = mgr.getEngineByName(Language.JAVA_SCRIPT);
                try {
                    Object value = engine.eval(question.getFormulae(), values);
                    if (Number.class.isAssignableFrom(question.getValueType())) {
                        if (!MathUtils.checkEquals(((Number) entry.getValue()).doubleValue(), ((Number) value).doubleValue(), 1.5)) {
                            errors.add(question.getNumber());
                        }
                    } else if (question.getValueType() == Boolean.class) {
                        if (!entry.getValue().equals(value)) {
                            errors.add(question.getNumber());
                        }
                    }
                } catch (ScriptException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        }

        LabTestResult result = new LabTestResult();
        int pointCount = getLabTest(locale).getQuestions().stream().
                filter(q -> errors.contains(q.getQuestionNumber())).
                mapToInt(LabTestQuestion::getPointCount).sum();

        final byte mark;
        if (pointCount >= 90 && pointCount <=100) {
            mark = 5;
        } else if (pointCount >= 70 && pointCount <=89) {
            mark = 4;
        } else if (pointCount >= 60 && pointCount <=69) {
            mark = 3;
        } else if (pointCount >= 0 && pointCount <=59) {
            mark = 2;
        } else {
            throw new IllegalArgumentException("Unexpected point count: " + pointCount);
        }
        result.setErrors(errors);
        result.setPointCount(pointCount);
        result.setCompleted(errors.size() <= 2);
        result.setMark(mark);

        return result;
    }

    @LogExecutionTime(500)
    @Override
    public byte[] printInitialData(V variant, Locale locale) {
        DRDataSource dataSource = new DRDataSource("parameterName", "parameterSign", "parameterValue", "parameterDimension");
        Map<String, Image> images = new HashMap<>();
        getInitialDataValues(variant, locale).forEach((value) -> {
            try {
                if (value.getValue() instanceof URL) {
                    images.put(value.getName(), ImageIO.read((URL) value.getValue()));
                } else {
                    dataSource.add(value.getName(), value.getSign(), String.valueOf(value.getValue()), value.getDimension());
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });

        JasperReportBuilder builder = report()
                .setTemplate(reportService.getReportTemplate(locale)).
                        title(reportService.createTitleComponent(
                                messageSource.getMessage("report.initial-data.title",
                                        new Object[]{getLabNumber()}, locale)));
        if (!images.isEmpty()) {
            HorizontalListBuilder imageListBuilder = cmp.horizontalList();
            for (Map.Entry<String, Image> image : images.entrySet()) {
                imageListBuilder.add(reportService.createImageWithTitle(image.getValue(), image.getKey()));
            }
            builder.title(imageListBuilder, cmp.verticalGap(20));
        }

        TextColumnBuilder<String> parameterNameColumn = col.column(messageSource.
                getMessage("report.lab-data.parameter-name", null, locale), "parameterName", type.stringType());
        TextColumnBuilder<String> parameterSignColumn = col.column(messageSource.
                getMessage("report.lab-data.parameter-sign", null, locale), "parameterSign", type.stringType())
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
        TextColumnBuilder<String> parameterValueColumn = col.column(messageSource.
                getMessage("report.lab-data.parameter-value", null, locale), "parameterValue", type.stringType())
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
        TextColumnBuilder<String> parameterDimensionColumn = col.column(messageSource.
                getMessage("report.lab-data.parameter-dimension", null, locale), "parameterDimension", type.stringType())
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);

        return reportService.printReport(
                builder.columns(parameterNameColumn, parameterSignColumn, parameterValueColumn, parameterDimensionColumn)
                        .setDataSource(dataSource));
    }

    @Override
    public Set<DataValue> getInitialDataValues(V data, Locale locale) {
        Set<DataValue> printData = new LinkedHashSet<>();
        for (Map.Entry<String, Object> entry : getInitialDataWithLocalizedValues(data, locale).entrySet()) {
            DataValue dataValue = new DataValue();
            dataValue.setName(messageSource.getMessage(entry.getKey(), null, locale));
            dataValue.setValue(entry.getValue());
            dataValue.setSign(messageSource.getMessage(entry.getKey() + "-sign", null, locale));
            dataValue.setDimension(messageSource.getMessage(entry.getKey() + "-dimension", null, locale));
            printData.add(dataValue);
        }
        return printData;
    }

    protected Map<String, Object> getInitialDataWithLocalizedValues(V data, Locale locale) {
        Map<String, Object> printData = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : getValuesFromModel(data).entrySet()) {
            if (entry.getValue() instanceof List) {
                List<?> value = (List) entry.getValue();
                if (value.isEmpty() || !(value.get(0) instanceof List)) {
                    List<String> captions = Arrays.asList(messageSource.getMessage(entry.getKey() + "-columns", null, locale).split(";"));
                    Map<String, Object> valueMap = IntStream.range(0, captions.size()).boxed().
                            collect(Collectors.toMap(captions::get, i -> getFieldValueForPrint(value.get(i), locale), (a, b) -> b, LinkedHashMap::new));
                    printData.put(entry.getKey(), valueMap);
                }
            } else {
                printData.put(entry.getKey(), getFieldValueForPrint(entry.getValue(), locale));
            }
        }
        return printData;
    }

    protected Object getFieldValueForPrint(Object value, Locale locale) {
        if (value instanceof Integer || value instanceof Map || value instanceof List) {
            return value;
        } else if (value instanceof Double) {
            return Precision.round((double) value, (double) value > 2.0 ? 2 : 3);
        } else if (value instanceof LocalDateTime) {
            return DATE_TIME_FORMATTER.format((TemporalAccessor) value);
        } else {
            return value instanceof Enum ? messageSource.getMessage(I18NUtils.getEnumName((Enum<?>) value), null, locale) : String.valueOf(value);
        }
    }

    protected Map<String, Object> getValuesForReport(T data, Locale locale) {
        Map<String, Object> values = new HashMap<>();

        UserInfo userInfo = userInfoService.getUserInfo(data.getUserLogin());
        if (userInfo.getGroup() == UserGroup.STUDENT) {
            StudentInfo studentInfo = studentInfoService.getStudentInfo(data.getUserLogin());
            values.put("teacherName", studentInfoService.getStudentTeacher(data.getUserLogin()));
            values.put("groupNumber", studentInfo.getGroup());
            values.put("teamNumber", studentInfo.getTeam() == null ? 0 : studentInfo.getTeam().getNumber());

            StringBuilder studentsList = new StringBuilder();
            for (String teamMember : studentInfoService.getTeamMembers(studentInfo.getTeam().getNumber())) {
                studentsList.append(UserInfoUtils.getShortInitials(userInfoService.getUserInfo(teamMember))).append('\n');
            }
            values.put("studentsList", studentsList.toString());
        } else {
            values.put("teacherName", UserInfoUtils.getShortInitials(userInfo));

            //todo убрать всё то ниже после релиза
            values.put("groupNumber", "");
            values.put("teamNumber", 0);

            values.put("studentsList",  "");
        }

        Map<String, Object> labVariantAndDataValues = getValuesFromModel(data.getVariant());

        labVariantAndDataValues.putAll(getValuesFromModel(data));
        values.putAll(labVariantAndDataValues.entrySet().stream().
                filter(e -> e.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey,
                e -> getFieldValueForPrint(e.getValue(), locale)))
        );

        return values;
    }

    private Map<String, Object> getValuesFromModel(DomainModel data) {
        Map<String, Object> values = new LinkedHashMap<>();
        ReflectionUtils.doWithFields(data.getClass(), field -> {
            ReflectionUtils.makeAccessible(field);
            Object value = ReflectionUtils.getField(field, data);
            if (value instanceof Valued) {
                values.put(field.getName(), ((Valued) value).value());
            } else if (value == null || value instanceof Enum) {
                values.put(field.getName(), value);
            } else {
                values.put(field.getName(), ReflectionUtils.getField(field, data));
            }
        }, field -> (field.getDeclaringClass() != LabVariant.class && field.getDeclaringClass() != IdentifiedDomainModel.class));
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

    protected T createBaseLabData(String userName) {
        T labData = createNewLabData();
        labData.setUserLogin(userName);
        labData.setStartDate(LocalDateTime.now());
        labData.setSaveDate(LocalDateTime.now());
        return labData;
    }

    private T getLastLabByUser(String userName, boolean completed) {
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

    /**
     * Генерирует вариант лабораторной, не сохраняя его
     * @return вариант лабораторной
     */
    protected abstract V generateNewLabVariant();
}
