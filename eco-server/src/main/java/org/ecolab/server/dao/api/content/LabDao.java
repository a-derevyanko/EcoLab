package org.ecolab.server.dao.api.content;

import org.ecolab.server.model.content.LabData;
import org.ecolab.server.model.content.LabTestQuestion;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Created by 777Al on 19.04.2017.
 */
public interface LabDao<T extends LabData> {
    T getLastLabByUser(long userId, boolean completed);

    void saveLab(T labData);

    int updateLab(T labData);

    int removeLabsByUser(long userId);

    int removeOldLabs(LocalDateTime lastSaveDate);

    Collection<LabTestQuestion> getTestQuestions();

    /**
     * Устанавливает признак пройденной лабораторной
     * @param data данные лабораторной
     */
    void setLabCompleted(T data);
}
