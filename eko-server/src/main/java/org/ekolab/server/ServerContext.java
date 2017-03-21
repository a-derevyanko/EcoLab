package org.ekolab.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;

/**
 * Created by Андрей on 06.09.2016.
 */
@Configuration
public class ServerContext {
    // TODO мб не нужен?
    @Bean
    public Validator localValidatorFactoryBean(@Autowired MessageSource messageSource) {
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setValidationMessageSource(messageSource);
        return validatorFactoryBean;
    }
}

