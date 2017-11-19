package org.ekolab.server.service.api.content;

import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.LabTest;
import org.ekolab.server.model.content.LabTestQuestionVariant;
import org.ekolab.server.model.content.LabVariant;
import org.ekolab.server.model.content.DataValue;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
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

    List<T> getAllLabsByUser(String userName);

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
     * @return номера вопросов, на которые был дан неверный ответ
     */
    List<Integer> checkLabTest(@Valid LabData<?> data, Map<LabTestQuestionVariant, Object> answers);

    int getLabNumber();
}
