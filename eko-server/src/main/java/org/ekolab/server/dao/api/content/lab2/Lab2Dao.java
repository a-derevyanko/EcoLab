package org.ekolab.server.dao.api.content.lab2;

import org.ekolab.server.dao.api.content.LabDao;
import org.ekolab.server.model.content.lab2.Lab2Data;
import org.ekolab.server.model.content.lab2.Lab2Variant;

/**
 * Created by 777Al on 19.04.2017.
 */
public interface Lab2Dao<T extends Lab2Variant> extends LabDao<Lab2Data<T>> {
}
