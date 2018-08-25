package org.ecolab.server.model.content;

import org.ecolab.server.model.DomainModel;

import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Поле, помеченное данной аннотацией редактируется пользователем и проверяется программой.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidatedBy {
    @NotNull
    Class<? extends FieldValidator<?, ? extends DomainModel>> value();
}
