package com.example.sams.response;

public record FoodResponse(
        Long foodId,

        String name,

        String producer,

        Float servingSize,

        String servingUnits,

        Integer kcal,

        Float protein,

        Float fat,

        Float carbs
) {
}
