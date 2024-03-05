package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = SpaceValidator.class)
@Documented
public @interface NoSpaces {
    String message() default "Spaces not allowed in login";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}
