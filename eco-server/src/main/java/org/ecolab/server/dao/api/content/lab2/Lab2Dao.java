package org.ecolab.server.dao.api.content.lab2;

import org.ecolab.server.dao.api.content.LabDao;
import org.ecolab.server.model.content.lab2.Lab2Data;
import org.ecolab.server.model.content.lab2.Lab2Variant;

/**
 * Created by 777Al on 19.04.2017.
 */
public interface Lab2Dao<T extends Lab2Variant> extends LabDao<Lab2Data<T>> {
}
