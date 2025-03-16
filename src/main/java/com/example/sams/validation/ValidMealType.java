package com.example.sams.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidMealTypeValidator.class)
public @interface ValidMealType {
    String message() default "Invalid meal type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}