package org.ekolab.server.service.api.content.lab1;

import org.ekolab.server.model.content.lab1.Lab1Data;
import org.ekolab.server.model.content.lab1.Lab1Variant;
import org.ekolab.server.service.api.content.LabService;

/**
 * Created by 777Al on 26.04.2017.
 */
public interface Lab1Service<T extends Lab1Data<V>, V extends Lab1Variant> extends LabService<T, V> {
}
