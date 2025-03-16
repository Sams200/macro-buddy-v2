package com.example.sams.request.user;

import com.example.sams.entity.Food;
import com.example.sams.entity.User;
import com.example.sams.enumeration.MealType;
import com.example.sams.validation.ValidMealType;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record EntryRequest(
        @NotNull(message = "Date required")
        LocalDate date,

        @NotEmpty(message = "Meal type required")
        @Size(max=15, message = "Meal type max size is 15")
        @ValidMealType(message = "Invalid meal type")
        String meal,

        @NotNull(message = "Food quantity required")
        @Positive(message = "Food quantity must be positive")
        Float quantity,

        @NotNull(message = "Food id required")
        Long foodId
) {
}
