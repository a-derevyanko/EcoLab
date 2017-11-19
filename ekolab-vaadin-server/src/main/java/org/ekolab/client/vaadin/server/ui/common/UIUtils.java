package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.data.Binder;
import com.vaadin.data.Converter;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.util.ReflectTools;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.server.model.DomainModel;
import org.ekolab.server.model.content.FieldValidationResult;
import org.ekolab.server.service.api.content.ValidationService;

import java.lang.reflect.Field;

/**
 * Created by 777Al on 29.06.2017.
 */
public abstract class UIUtils {
    public static Converter<String, ?> getStringConverter(Field field, I18N i18N) {
        Class<?> propClass = ReflectTools.convertPrimitiveType(field.getType());
        return getStringConverter(propClass, i18N);
    }

    public static <T> Converter<String, T> getStringConverter(Class<T> propClass, I18N i18N) {
        if (propClass == Integer.class) {
            return (Converter<String, T>) new StringToIntegerConverter(i18N.get("validator.must-be-number"));
        } else if (propClass == Double.class) {
            return (Converter<String, T>) new StringToDoubleConverter(i18N.get("validator.must-be-double"));
        } else {
            return null;
        }
    }

    /**
     * Добавляет валидатор, который проверяет правильность значения поля
     */
    public static <M extends DomainModel> void bindField(Field propertyField,
                                                        Binder.BindingBuilder<?, ?> builder,
                                                        Binder<M> dataBinder,
                                                        ValidationService validationService,
                                                        I18N i18N) {
        if (validationService.isFieldValidated(propertyField)) {
            builder.withValidator((value, context) -> {
                if (value == null) {
                    return ValidationResult.ok();
                }

                FieldValidationResult result = validationService.getFieldValidator(propertyField).validate(value, dataBinder.getBean());
                return result.isError() ? ValidationResult.error(i18N.get(result.getErrorMessage())) : ValidationResult.ok();
            });
        }
        builder.bind(propertyField.getName());
    }
}
