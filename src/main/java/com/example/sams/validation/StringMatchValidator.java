package com.example.sams.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class StringMatchValidator implements ConstraintValidator<StringMatch, Object> {

    private String first;
    private String second;
    private String message;

    @Override
    public void initialize(StringMatch constraintAnnotation) {
        this.first = constraintAnnotation.first();
        this.second = constraintAnnotation.second();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        Object firstValue = new BeanWrapperImpl(obj).getPropertyValue(first);
        Object secondValue = new BeanWrapperImpl(obj).getPropertyValue(second);

        boolean isValid = first != null && first.equals(second);

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(second)
                    .addConstraintViolation();
        }

        return isValid;
    }
}