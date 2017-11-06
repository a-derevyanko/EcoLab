package org.ekolab.server.service.impl.content;

import org.ekolab.server.model.content.FieldValidator;
import org.ekolab.server.model.content.LabData;
import org.ekolab.server.model.content.LabVariant;
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
    @Cacheable("FIELD_VALIDATORS")
    public <T extends LabData<V>, V extends LabVariant> FieldValidator<Object, V, T> getFieldValidator(Field field) {
        ValidatedBy annotation = AnnotationUtils.findAnnotation(field, ValidatedBy.class);
        return annotation == null ? null : ctx.getBean((Class<FieldValidator<Object, V, T>>)annotation.value());
    }
}
