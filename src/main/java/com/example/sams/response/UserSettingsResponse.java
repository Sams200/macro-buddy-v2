package com.example.sams.response;

public record UserSettingsResponse(
        Integer dailyKcalGoal,
        Float dailyProteinGoal,
        Float dailyFatGoal,
        Float dailyCarbGoal
) {
}
