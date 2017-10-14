package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.data.Converter;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractField;
import com.vaadin.util.ReflectTools;
import org.ekolab.client.vaadin.server.service.impl.I18N;

import java.lang.reflect.Field;

/**
 * Created by 777Al on 29.06.2017.
 */
public abstract class UIUtils {
    public static Converter<String, ?> getStringConverter(Field field, I18N i18N) {
        Class<?> propClass = ReflectTools.convertPrimitiveType(field.getType());
        return getStringConverter(propClass, field.getName(), i18N);
    }

    public static <T> Converter<String, T> getStringConverter(Class<T> propClass, String fieldName, I18N i18N) {
        String validatorPrefix = i18N.get("validator.value-of-field") + " '"
                + i18N.get(fieldName) + "' ";
        if (propClass == Integer.class) {
            return (Converter<String, T>) new StringToIntegerConverter(validatorPrefix + i18N.get("validator.must-be-number"));
        } else if (propClass == Double.class) {
            return (Converter<String, T>) new StringToDoubleConverter(validatorPrefix + i18N.get("validator.must-be-double"));
        } else {
            return null;
        }
    }

    public static <T> void addValidator(AbstractField<T> field, Validator<T> validator) {
        field.addValueChangeListener(event -> {
            ValidationResult result = validator.apply(event.getValue(), new ValueContext(field));

            if (result.isError()) {
                UserError error = new UserError(result.getErrorMessage());
                field.setComponentError(error);
            } else {
                field.setComponentError(null);
            }
        });
    }
}
