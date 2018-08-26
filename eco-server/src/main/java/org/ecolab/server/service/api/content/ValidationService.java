package org.ecolab.server.service.api.content;

import org.ecolab.server.model.DomainModel;
import org.ecolab.server.model.content.FieldValidator;

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
