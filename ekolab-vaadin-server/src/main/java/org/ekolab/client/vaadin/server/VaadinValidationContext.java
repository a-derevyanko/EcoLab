package org.ekolab.client.vaadin.server;

import com.vaadin.data.validator.BeanValidator;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;
import org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.validation.ConstraintViolation;
import javax.validation.MessageInterpolator;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Locale;

/**
 * Кастомизация {@code com.vaadin.data.validator.BeanValidator}
 * Нужно проверять работоспособность при обновлении версии Vaadin
 */
@SpringBootConfiguration
public class VaadinValidationContext extends WebMvcConfigurerAdapter {
    private final MessageSource messageSource;

    public VaadinValidationContext(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Валидатору задаётся Кастомная фабрика
     * Нужен, т. к. {@code com.vaadin.data.validator.BeanValidator.LazyFactoryInitializer#getFactory()}
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initializeValidator() throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        Field field = Class.forName(BeanValidator.class.getName()+ "$LazyFactoryInitializer").getDeclaredField("FACTORY");
        ReflectionUtils.makeAccessible(field);
        //'modifiers' - it is a field of a class called 'Field'. Make it accessible and remove
        //'final' modifier for our 'CONSTANT' field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        ReflectionUtils.makeAccessible(modifiersField);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, validatorFactory());
    }

    @Bean
    @Override
    public Validator getValidator() {
        return validatorFactory();
    }

    @Bean
    public javax.validation.Validator validator() {
        return validatorFactory();
    }

    @Bean
    public OptionalValidatorFactoryBean validatorFactory() {
        OptionalValidatorFactoryBean validatorFactoryBean = new OptionalValidatorFactoryBean();
        validatorFactoryBean.setMessageInterpolator(messageInterpolator());
        return validatorFactoryBean;
    }

    @Bean
    public MessageInterpolator messageInterpolator() {
        return new EkoLabMessageInterpolatorImpl(messageSource);
    }

    private class EkoLabMessageInterpolatorImpl extends ResourceBundleMessageInterpolator {
        private final MessageSource messageSource;
        private final Class<?> contextImplClass;
        private final Field violationField;

        public EkoLabMessageInterpolatorImpl(MessageSource messageSource) {
            super(new MessageSourceResourceBundleLocator(messageSource));
            this.messageSource = messageSource;
            try {
                contextImplClass = Class.forName(BeanValidator.class.getName()+ "$ContextImpl");
                violationField = contextImplClass.getDeclaredField("violation");
                ReflectionUtils.makeAccessible(violationField);
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException(e);
            }
        }

        @Override
        public String interpolate(String message, Context context, Locale locale) {
            if (contextImplClass.isAssignableFrom(context.getClass())) {
                try {
                    return messageSource.getMessage("validator.value-of-field", null, locale) + " '"
                            + messageSource.getMessage(String.valueOf(((ConstraintViolation<?>) violationField.get(context)).getPropertyPath()), null, locale) + "'. " +
                            super.interpolate(message, context, locale);
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException(e);
                }
            } else {
                return super.interpolate(message, context, locale);
            }
        }
    }
}
