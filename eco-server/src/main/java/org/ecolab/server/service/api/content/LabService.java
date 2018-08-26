package org.ecolab.server.service.api.content;

import org.ecolab.server.model.content.DataValue;
import org.ecolab.server.model.content.LabData;
import org.ecolab.server.model.content.LabTest;
import org.ecolab.server.model.content.LabTestQuestionVariant;
import org.ecolab.server.model.content.LabTestResult;
import org.ecolab.server.model.content.LabVariant;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by 777Al on 26.04.2017.
 */
@Validated
public interface LabService<T extends LabData<V>, V extends LabVariant> {

    boolean isFieldCalculated(Field field);

    T getLastUncompletedLabByUser(String userName);

    T getCompletedLabByUser(String userName);

    T startNewLab(String userName);

    T updateLab(@Valid T labData);

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
     */
    void updateCalculatedFields(T labData);

    /**
     * Возвращает печатный вариант исходных данных в PDF формате
     * @param variant вариант лабораторной работы
     * @param locale язык
     * @return печатный вариант исходных данных в PDF формате
     */
    byte[] printInitialData(V variant, Locale locale);

    Set<DataValue> getInitialDataValues(V data, Locale locale);

    LabTest getLabTest(Locale locale);

    /**
     * Проверяет результаты теста.
     * @param data данные лабораторной
     * @param answers ответы
     * @param locale локализация
     * @return оценка и номера вопросов, на которые был дан неверный ответ
     */
    LabTestResult checkLabTest(@Valid LabData<?> data, Map<LabTestQuestionVariant, Object> answers, Locale locale);

    int getLabNumber();

    /**
     * Устанавливает признак пройденной лабораторной
     * @param data данные лабораторной
     */
    void setTestCompleted(T data);
}
