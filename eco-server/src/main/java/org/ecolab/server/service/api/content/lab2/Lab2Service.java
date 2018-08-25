package org.ecolab.server.service.api.content.lab2;

import org.ecolab.server.model.content.lab2.Lab2Data;
import org.ecolab.server.model.content.lab2.Lab2Variant;
import org.ecolab.server.service.api.content.LabService;

/**
 * Created by 777Al on 26.04.2017.
 */
public interface Lab2Service<V extends Lab2Variant> extends LabService<Lab2Data<V>, V> {
}
