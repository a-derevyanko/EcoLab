package org.ekolab.server.service.api.content;

import org.ekolab.server.model.LabData;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    byte[] createReport(T labData);

    /**
     * Заполняет в модели значения вычисляемых полей
     * @param labData модель
     * @return модель, в которой заполнены вычисляемые поля
     */
    T updateCalculatedFields(T labData);

    /**
     * Создаёт структуру с данными лабораторной, не сохраняя её
     * @return новая структура с данными лабораторной, не сохраняя её
     */
    T createNewLabData();

    /**
     * Возвращает данные варианта лабораторной
     * @return данные варианта.
     */
    Map<String,String> getInitialData();
}
