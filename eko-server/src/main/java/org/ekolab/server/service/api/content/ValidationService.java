package org.ekolab.server.service.api.content;

import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.LabVariant;

import java.lang.reflect.Field;

public interface ValidationService<T extends LabData<V>, V extends LabVariant> {
    FieldValidator<V, T> getFieldValidator(Field field);
}
