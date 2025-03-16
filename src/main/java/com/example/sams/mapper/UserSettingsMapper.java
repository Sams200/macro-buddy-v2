package com.example.sams.mapper;

import com.example.sams.entity.UserSettings;
import com.example.sams.response.UserSettingsResponse;
import org.springframework.stereotype.Service;

@Service
public class UserSettingsMapper {
    public UserSettingsResponse toUserSettingsResponse(UserSettings settings) {
        return new UserSettingsResponse(
                settings.getGoalKcal(),
                settings.getGoalProtein(),
                settings.getGoalFat(),
                settings.getGoalCarbs()
        );
    }
}
