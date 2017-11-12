package org.ekolab.server.service.api.content;

import org.ekolab.server.model.DomainModel;
import org.ekolab.server.model.content.FieldValidator;

import java.lang.reflect.Field;

public interface ValidationService {
    /**
     * Возвращает признак того, что значение поля может быть проверено программой
     * @param field поле
     * @return признак того, что значение поля может быть проверено программой
     */
    boolean isFieldValidated(Field field);

    <M extends DomainModel> FieldValidator<Object, M> getFieldValidator(Field field);
}
