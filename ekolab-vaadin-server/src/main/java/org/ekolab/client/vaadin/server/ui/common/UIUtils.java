package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.data.Converter;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.util.ReflectTools;
import org.ekolab.client.vaadin.server.service.I18N;

import java.lang.reflect.Field;

/**
 * Created by 777Al on 29.06.2017.
 */
public abstract class UIUtils {
    public static Converter<String, ?> getStringConverter(Field field, I18N i18N) {
        Class<?> propClass = ReflectTools.convertPrimitiveType(field.getType());
        String validatorPrefix = i18N.get("validator.value-of-field") + " '"
                + i18N.get(field.getName()) + "' ";
        if (propClass == Integer.class) {
            return new StringToIntegerConverter(validatorPrefix + i18N.get("validator.must-be-number"));
        } else if (propClass == Double.class) {
            return new StringToDoubleConverter(validatorPrefix + i18N.get("validator.must-be-double"));
        } else {
            return null;
        }
    }
}
