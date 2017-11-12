package org.ekolab.server.service.impl.content;

import org.ekolab.server.model.DomainModel;
import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.ValidatedBy;
import org.ekolab.server.service.api.content.ValidationService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
public class ValidationServiceImpl implements ValidationService {
    private final ApplicationContext ctx;

    public ValidationServiceImpl(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public boolean isFieldValidated(Field field) {
        return field.getAnnotation(ValidatedBy.class) != null;
    }

    @Override
    @Cacheable("FIELD_VALIDATORS")
    public <M extends DomainModel> FieldValidator<Object, M> getFieldValidator(Field field) {
        ValidatedBy annotation = AnnotationUtils.findAnnotation(field, ValidatedBy.class);
        if (annotation == null) {
            throw new IllegalArgumentException("No validator for: " + field);
        } else {
            return ctx.getBean((Class<FieldValidator<Object, M>>) annotation.value());
        }
    }
}
