package org.ekolab.server.dao.api.content;

import org.ekolab.server.model.LabData;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by 777Al on 19.04.2017.
 */
public interface LabDao<T extends LabData> {
    T getLastLabByUser(String userName);

    List<T> getAllLabsByUser(String userName);

    long saveLab(T labData);

    int updateLab(T labData);

    int removeLabsByUser(String userName);

    int removeOldLabs(LocalDateTime lastSaveDate);
}
