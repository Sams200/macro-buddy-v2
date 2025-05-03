package com.example.sams.request.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UserSettingsRequest(
        @NotNull(message = "Calories required")
        @Min(value=0, message = "Calories must be greater or equal to 0")
        Integer goalKcal,

        @NotNull(message = "Protein required")
        @Min(value=0, message = "Protein must be greater or equal to 0")
        Float goalProtein,

        @NotNull(message = "Fat required")
        @Min(value=0, message = "Fat must be greater or equal to 0")
        Float goalFat,

        @NotNull(message = "Carbs required")
        @Min(value=0, message = "Carbs must be greater or equal to 0")
        Float goalCarbs,

        @NotNull(message = "Water required")
        @Min(value=0, message = "Water must be greater or equal to 0")
        Integer goalWater
) {
}
