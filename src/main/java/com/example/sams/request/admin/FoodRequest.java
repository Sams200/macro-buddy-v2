package com.example.sams.request.admin;

import jakarta.validation.constraints.*;
import lombok.With;
import org.springframework.web.multipart.MultipartFile;

@With
public record FoodRequest (

    @NotEmpty(message = "Food name required")
    @Size(max=100, message = "Food name can have at most 100 characters")
    String name,

    @NotEmpty(message = "Food producer required")
    @Size(max=50, message = "Food producer can have at most 50 characters")
    String producer,

    @NotNull(message = "Food serving size required")
    @Positive(message = "Food serving size must be greater than 0")
    Float servingSize,

    @NotEmpty(message = "Food serving units required")
    @Size(max=20, message = "Food serving units can have at most 20 characters")
    String servingUnits,

    @NotNull(message = "Food Calories required")
    @Min(value=0, message = "Food Calories must be greater or equal to 0")
    Integer kcal,

    @NotNull(message = "Food protein required")
    @Min(value=0, message = "Food protein must be greater or equal to 0")
    Float protein,

    @NotNull(message = "Food fat required")
    @Min(value=0, message = "Food fat must be greater or equal to 0")
    Float fat,

    @NotNull(message = "Food carbs required")
    @Min(value=0, message = "Food carbs must be greater or equal to 0")
    Float carbs
){
    public FoodRequest withMacro(Integer kcal, Float protein, Float fat, Float carbs) {
        return new FoodRequest(
                this.name,
                this.producer,
                this.servingSize,
                this.servingUnits,
                kcal,
                protein,
                fat,
                carbs
                );
    }

}
