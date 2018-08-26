package org.ecolab.server.service.api.content.lab1;

import org.ecolab.server.model.content.lab1.Lab1Data;
import org.ecolab.server.model.content.lab1.Lab1Variant;
import org.ecolab.server.service.api.content.LabService;

/**
 * Created by 777Al on 26.04.2017.
 */
public interface Lab1Service<V extends Lab1Variant> extends LabService<Lab1Data<V>, V> {
}
