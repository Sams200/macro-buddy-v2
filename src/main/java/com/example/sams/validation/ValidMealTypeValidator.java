package com.example.sams.validation;

import com.example.sams.enumeration.MealType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidMealTypeValidator implements ConstraintValidator<ValidMealType, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false; // Null values are handled by @NotEmpty
        }
        return MealType.isValidMealType(value); // Check if the value is a valid MealType
    }
}