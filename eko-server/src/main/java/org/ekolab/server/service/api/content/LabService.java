package org.ekolab.server.service.api.content;

import org.ekolab.server.model.LabData;
import org.ekolab.server.model.content.lab3.Lab3Data;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by 777Al on 26.04.2017.
 */
public interface LabService<T extends LabData> {
    boolean isFieldCalculated(Field field);

    T getLastLabByUser(String userName);

    List<T> getAllLabsByUser(String userName);

    T saveLab(T labData);

    T updateLab(T labData);

    int removeLabsByUser(String userName);

    int removeOldLabs(LocalDateTime lastSaveDate);

    byte[] createReport(Lab3Data labData);
}
