package com.example.sams.enumeration;

import java.util.Arrays;

public enum MealType {

    /*
    an enumeration of all the meal types and water
     */
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner"),
    SNACK("Snacks"),
    WATER("Water");

    private final String meal;

    public String getMeal(){
        return meal;
    }

    MealType(String meal) {
        this.meal = meal;
    }

    public static MealType getInstance(String meal){
    /*
    given a string, get the enumeration
     */
        return switch (meal.charAt(0)) {
            case 'W' -> WATER;
            case 'B' -> BREAKFAST;
            case 'L' -> LUNCH;
            case 'D' -> DINNER;
            default -> SNACK;
        };
    }

    public static boolean isValidMealType(String value) {
        return Arrays.stream(MealType.values())
                .anyMatch(mealType -> mealType.name().equalsIgnoreCase(value));
    }
}