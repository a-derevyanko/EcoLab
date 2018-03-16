package org.ekolab.client.vaadin.server.ui.common;

import com.vaadin.data.Binder;
import com.vaadin.data.Converter;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.ui.UI;
import com.vaadin.util.ReflectTools;
import org.ekolab.client.vaadin.server.service.impl.I18N;
import org.ekolab.server.model.DomainModel;
import org.ekolab.server.model.content.FieldValidationResult;
import org.ekolab.server.service.api.content.ValidationService;
import org.omg.CORBA.portable.UnknownException;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by 777Al on 29.06.2017.
 */
public abstract class UIUtils {
    private static class StringToDoubleConverterWithMaximumFractionDigits extends StringToDoubleConverter {

        public StringToDoubleConverterWithMaximumFractionDigits(I18N i18N) {
            super(i18N.get("validator.must-be-double",
                    DecimalFormatSymbols.getInstance(UI.getCurrent().getLocale()).getDecimalSeparator()));
        }

        @Override
        protected NumberFormat getFormat(Locale locale) {
            NumberFormat format = super.getFormat(locale);
            format.setMaximumFractionDigits(6);
            return format;
        }
    }

    public static Converter<String, ?> getStringConverter(Field field, I18N i18N) {
        Class<?> propClass = ReflectTools.convertPrimitiveType(field.getType());
        return getStringConverter(propClass, i18N);
    }

    public static <T> Converter<String, T> getStringConverter(Class<T> propClass, I18N i18N) {
        if (propClass == Integer.class) {
            return (Converter<String, T>) new StringToIntegerConverter(i18N.get("validator.must-be-number"));
        } else if (propClass == Double.class) {
            return (Converter<String, T>) new StringToDoubleConverterWithMaximumFractionDigits(i18N);
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

    public static boolean isModelFull(DomainModel model) {
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(model.getClass());
        try {
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                Method readMethod = descriptor.getReadMethod();
                if (readMethod.invoke(model) == null) {
                    return false;
                }
            }
        } catch (ReflectiveOperationException e) {
            throw new UnknownException(e);
        }
        return true;
    }
}
