package org.ekolab.server.dao.api.content;

import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.LabTestQuestion;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Created by 777Al on 19.04.2017.
 */
public interface LabDao<T extends LabData> {
    T getLastLabByUser(String userName, boolean completed);

    List<T> getAllLabsByUser(String userName);

    void saveLab(T labData);

    int updateLab(T labData);

    int removeLabsByUser(String userName);

    int removeOldLabs(LocalDateTime lastSaveDate);

    Collection<LabTestQuestion> getTestQuestions(Locale locale);
}
