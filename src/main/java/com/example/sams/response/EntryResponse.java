package com.example.sams.response;

import com.example.sams.entity.Food;
import lombok.With;

import java.time.LocalDate;

@With
public record EntryResponse(
        Long entryId,

        LocalDate date,

        String meal,

        Float quantity,

        Food food
) {
}
