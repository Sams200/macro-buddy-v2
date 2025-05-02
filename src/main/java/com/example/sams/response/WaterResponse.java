package com.example.sams.response;

import java.time.LocalDate;

public record WaterResponse(
        Long waterId,
        LocalDate date,
        Integer quantity
) {
}
