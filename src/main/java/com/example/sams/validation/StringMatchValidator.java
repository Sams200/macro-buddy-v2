package com.example.sams.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import static org.springframework.security.util.FieldUtils.getFieldValue;

public class StringMatchValidator implements ConstraintValidator<StringMatch, Object> {

    private String first;
    private String second;

    @Override
    public void initialize(StringMatch constraintAnnotation) {
        this.first = constraintAnnotation.first();
        this.second = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            Object firstValue = getFieldValue(obj, first);
            Object secondValue = getFieldValue(obj, second);

            return firstValue != null && firstValue.equals(secondValue);
        }
        catch (Exception e) {
            return false;
        }
    }
}