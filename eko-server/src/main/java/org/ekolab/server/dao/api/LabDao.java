package org.ekolab.server.dao.api;

import org.ekolab.server.model.Lab3Data;
import org.ekolab.server.model.LabData;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by 777Al on 19.04.2017.
 */
public interface LabDao<T extends LabData> {
    Lab3Data getLastLabByUser(String userName);

    List<Lab3Data> getAllLabsByUser(String userName);

    T saveLab(T labData);

    T updateLab(T labData);

    void removeLabsByUser(String userName);

    void removeOldLabs(LocalDateTime lastSaveDate);
}
