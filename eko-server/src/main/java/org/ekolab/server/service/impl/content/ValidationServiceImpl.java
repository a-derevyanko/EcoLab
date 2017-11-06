package org.ekolab.server.service.impl.content;

import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.LabVariant;
import org.ekolab.server.model.content.ValidatedBy;
import org.ekolab.server.service.api.content.ValidationService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class ValidationServiceImpl implements ValidationService {
    private final List<FieldValidator<?, ?, ?>> validators;

    public ValidationServiceImpl(List<FieldValidator<?, ?, ?>> validators) {
        this.validators = validators;
    }

    @Override
    @Cacheable("FIELD_VALIDATORS")
    public <T extends LabData<V>, V extends LabVariant> FieldValidator<Object, V, T> getFieldValidator(Field field) {
        ValidatedBy annotation = AnnotationUtils.findAnnotation(field, ValidatedBy.class);
        if (annotation == null) {
            return null;
        } else {
            return (FieldValidator<Object, V, T>) validators.stream().
                    filter(validator -> annotation.value().isAssignableFrom(validator.getClass())).
                    reduce((u, v) -> {throw new IllegalStateException("More than one validator found");}).orElseThrow(() -> new IllegalArgumentException(field.getName()));
        }
    }
}
