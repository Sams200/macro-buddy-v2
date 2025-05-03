package com.example.sams.request.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record WaterRequest(
    @NotNull
    LocalDate date,

    @NotNull(message = "Water quantity required")
    @Min(value=0, message = "Water quantity must be positive")
    Integer quantity
) {
}
