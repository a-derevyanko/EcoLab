package org.ekolab.server.service.api.content;

import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.LabVariant;
import org.springframework.mail.MailException;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

/**
 * Created by 777Al on 26.04.2017.
 */
public interface LabService<T extends LabData> {
    boolean isFieldCalculated(Field field);

    T getLastUncompletedLabByUser(String userName);

    List<T> getAllLabsByUser(String userName);

    T startNewLab(String userName);

    T updateLab(T labData);

    int removeLabsByUser(String userName);

    int removeOldLabs(LocalDateTime lastSaveDate);

    /**
     * Возвращает печатный вариант отчёта в PDF формате
     * @param labData данные лабораторной работы
     * @param locale язык
     * @return печатный вариант данных в PDF формате
     */
    byte[] createReport(T labData, Locale locale);

    /**
     * Заполняет в модели значения вычисляемых полей
     * @param labData модель
     * @return модель, в которой заполнены вычисляемые поля
     */
    T updateCalculatedFields(T labData);

    /**
     * Создаёт структуру с данными лабораторной, не сохраняя её
     * @return новая структура с данными лабораторной
     */
    T createNewLabData();

    /**
     * Возвращает печатный вариант исходных данных в PDF формате
     * @param variant вариант лабораторной работы
     * @param locale язык
     * @return печатный вариант исходных данных в PDF формате
     */
    byte[] printInitialData(LabVariant variant, Locale locale);

    /**
     * Отправляет печатный вариант исходных данных в PDF формате на почту
     * @param variant вариант лабораторной работы
     * @param locale язык
     * @param email адрес
     */
    void sentInitialDataToEmail(LabVariant variant, Locale locale, String email) throws MailException;


    /**
     * Отправляет печатный вариант отчёта в PDF формате на почту
     * @param labData данные лабораторной работы
     * @param locale язык
     * @param email адрес
     */
    void sendReportToEmail(T labData, Locale locale, String email);
}
