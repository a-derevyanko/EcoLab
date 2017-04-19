package org.ekolab.server.dao.api;

import org.ekolab.server.model.LabData;

/**
 * Created by 777Al on 19.04.2017.
 */
public interface LabDao<T extends LabData> {
    T getLabByUser(String userName);

    T saveLab(T labData);

    void updateLab(T labData);
}
