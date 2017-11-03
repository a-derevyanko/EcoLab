package org.ekolab.server.model.content;

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
public @interface Validated {
    @NotNull
    Class<? extends FieldValidator<? extends LabVariant,? extends LabData<? extends LabVariant>>> value();
}
