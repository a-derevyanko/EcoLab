package org.ekolab.server.service.impl.content;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.jasperreports.engine.JRDataSource;
import org.ekolab.server.common.UserInfoUtils;
import org.ekolab.server.dao.api.content.LabDao;
import org.ekolab.server.dev.LogExecutionTime;
import org.ekolab.server.model.DomainModel;
import org.ekolab.server.model.UserInfo;
import org.ekolab.server.model.content.Calculated;
import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.LabVariant;
import org.ekolab.server.model.content.Validated;
import org.ekolab.server.service.api.UserInfoService;
import org.ekolab.server.service.api.content.LabService;
import org.ekolab.server.service.impl.ReportTemplates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.mail.MailException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

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
    protected JavaMailSender mailSender;

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
        T labData = createBaseLabData(userName);
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
    public void sentInitialDataToEmail(LabVariant variant, Locale locale, String email) throws MailException {
        byte[] data = printInitialData(variant, locale);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
            mimeMessageHelper.setFrom("ekolabserver@gmail.com");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setText(messageSource.getMessage("report.initial-data.email-data-in-attach", null, locale));
            mimeMessageHelper.setSubject(messageSource.getMessage("report.initial-data.email-subject", null, locale));
            mimeMessageHelper.addAttachment("initialData.pdf", new ByteArrayDataSource(data, MediaType.APPLICATION_PDF_VALUE));

        } catch (MessagingException e) {
            throw new MailPreparationException(e.getLocalizedMessage(), e);
        }
        mailSender.send(mimeMessage);
    }

    @Override
    public void sendReportToEmail(T labData, Locale locale, String email) {

    }

    @LogExecutionTime(500)
    protected byte[] printInitialData(LabVariant variant, int labNumber, Locale locale) {
        TextColumnBuilder<String> parameterNameColumn = col.column(messageSource.
                getMessage("report.lab-data.parameter-name", null, locale), "parameterName", type.stringType());
        TextColumnBuilder<String> parameterValueColumn = col.column(messageSource.
                getMessage("report.lab-data.parameter-value", null, locale), "parameterValue", type.stringType())
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
        return ReportTemplates.printReport(report()
                .setTemplate(ReportTemplates.reportTemplate(locale))
                .title(ReportTemplates.createTitleComponent(messageSource.getMessage("report.initial-data.title", new Object[]{labNumber}, locale)))
                .columns(parameterNameColumn, parameterValueColumn)
                .setDataSource(createDataSourceFromModel(variant, locale)));
    }

    protected JRDataSource createDataSourceFromModel(DomainModel data, Locale locale) {
        DRDataSource dataSource = new DRDataSource("parameterName", "parameterValue");
        for (Field field : data.getClass().getDeclaredFields()) {
            ReflectionUtils.makeAccessible(field);
            Object value = ReflectionUtils.getField(field, data);
            String name = messageSource.getMessage(field.getName(), null, locale);
            if (value.getClass().isEnum()) {
                dataSource.add(name, messageSource.getMessage(value.getClass().getSimpleName()
                        + '.' + ((Enum<?>) value).name(), null, locale));
            } else {
                dataSource.add(name, String.valueOf(ReflectionUtils.getField(field, data)));
            }
        }
        return dataSource;
    }

    protected JasperReportBuilder createBaseLabReport(LabData<?> labData, int labNumber, Locale locale) {
        UserInfo userInfo = userInfoService.getUserInfo(labData.getUserLogin());

        TextColumnBuilder<String> parameterNameColumn = col.column(messageSource.getMessage("report.lab-data.parameter-name", null, locale), "parameterName", type.stringType());
        TextColumnBuilder<String> parameterValueColumn = col.column(messageSource.getMessage("report.lab-data.parameter-value", null, locale), "parameterValue", type.stringType())
                .setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
        return report()
                .setTemplate(ReportTemplates.reportTemplate(locale))
                .title(ReportTemplates.createTitleComponent(messageSource.
                        getMessage("report.lab-data.title", new Object[]{labNumber, UserInfoUtils.getShortInitials(userInfo)}, locale)))
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

    protected boolean validateFieldValue(String fieldName, Object value, T labData) {
        return true;
    }

    /**
     * Генерирует вариант лабораторной, не сохраняя его
     * @return вариант лабораторной
     */
    protected abstract V generateNewLabVariant();
}
