package org.ekolab.server.service.impl.content;

import org.apache.commons.lang.UnhandledException;
import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.LabVariant;
import org.ekolab.server.model.content.Validated;
import org.ekolab.server.service.api.content.ValidationService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
public class ValidationServiceImpl<T extends LabData<V>, V extends LabVariant> implements ValidationService<T, V> {
    @Override
    @Cacheable("FIELD_VALIDATORS")
    public FieldValidator<V, T> getFieldValidator(Field field) {
        Validated annotation = AnnotationUtils.findAnnotation(field, Validated.class);
        if (annotation == null) {
            return null;
        } else {
            try {
                return (FieldValidator<V, T>) annotation.value().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
               throw new UnhandledException(e);
            }
        }
    }
}
