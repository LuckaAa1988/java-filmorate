package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = DateValidator.class)
@Documented
public @interface AfterDate {
    String message() default "Date must not be before {current}";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
    String current();
}
