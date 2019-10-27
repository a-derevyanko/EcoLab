package org.ecolab.client.vaadin.server.ui.common;

import com.vaadin.data.Binder;
import com.vaadin.data.Converter;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.util.ReflectTools;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.ecolab.client.vaadin.server.service.impl.I18N;
import org.ecolab.server.model.DomainModel;
import org.ecolab.server.model.content.FieldValidationResult;
import org.ecolab.server.service.api.content.ValidationService;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
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
            var format = super.getFormat(locale);
            format.setMaximumFractionDigits(6);
            return format;
        }
    }

    public static Converter<String, ?> getStringConverter(Field field, I18N i18N) {
        var propClass = ReflectTools.convertPrimitiveType(field.getType());
        return getStringConverter(propClass, i18N);
    }

    @SuppressWarnings("unchecked")
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

                var result = validationService.getFieldValidator(propertyField).validate(value, dataBinder.getBean());
                return result.isError() ? ValidationResult.error(i18N.get(result.getErrorMessage())) : ValidationResult.ok();
            });
        }
        builder.bind(propertyField.getName());
    }

    public static boolean isModelFull(DomainModel model) {
        return FieldUtils.getAllFieldsList(model.getClass()).stream()
                .filter(f -> f.getAnnotation(Nullable.class) == null).noneMatch(field -> {
                    field.setAccessible(true);
                    return ReflectionUtils.getField(field, model) == null;
                });
    }

    public static Notification showNotification(String caption, String description) {
        var notification = new Notification("Welcome to EcoLab");

        notification.setCaption(caption);
        notification.setDescription(description);
        notification.setHtmlContentAllowed(true);
        notification.setStyleName("tray dark small closable login-help");
        notification.setPosition(Position.BOTTOM_CENTER);
        notification.setDelayMsec(2000000);
        notification.show(Page.getCurrent());
        return notification;
    }
}
