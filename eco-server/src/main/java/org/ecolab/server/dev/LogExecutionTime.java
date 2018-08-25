package org.ecolab.server.dev;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация, помечая метод которой можно включитб логгирование времени выполнения метода.
 * Обрабатываетя соответсвующим аспектом.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {
    long value() default 0;
}