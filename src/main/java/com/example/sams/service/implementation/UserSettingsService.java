package com.example.sams.service.implementation;

import com.example.sams.entity.User;
import com.example.sams.entity.UserSettings;
import com.example.sams.exception.ResourceNotFoundException;
import com.example.sams.mapper.UserMapper;
import com.example.sams.mapper.UserSettingsMapper;
import com.example.sams.repo.UserRepo;
import com.example.sams.repo.UserSettingsRepo;
import com.example.sams.request.user.UserSettingsRequest;
import com.example.sams.response.UserSettingsResponse;
import com.example.sams.service.IUserSettingsService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserSettingsService implements IUserSettingsService {
    private final UserRepo userRepo;
    private final UserSettingsRepo userSettingsRepo;
    private final UserSettingsMapper userSettingsMapper;

    @Override
    public UserSettingsResponse findAuthenticatedUserData(Authentication authentication) {
        User user = ((User) authentication.getPrincipal());
        UserSettings userSettings=
                userSettingsRepo.findByUser_UserId(user.getUserId())
                .orElseThrow(()->new ResourceNotFoundException("User settings not found"));

        return userSettingsMapper.toUserSettingsResponse(userSettings);
    }

    @Override
    public UserSettingsResponse update(Authentication authentication, UserSettingsRequest request) {
        User user = ((User) authentication.getPrincipal());
        UserSettings userSettings = userSettingsRepo.findByUser_UserId(user.getUserId())
                .orElse(new UserSettings());

        userSettings.setGoalKcal(request.goalKcal());
        userSettings.setGoalProtein(request.goalProtein());
        userSettings.setGoalFat(request.goalFat());
        userSettings.setGoalCarbs(request.goalCarbs());
        userSettings.setUser(user);

        UserSettings savedSettings = userSettingsRepo.save(userSettings);

        return userSettingsMapper.toUserSettingsResponse(savedSettings);
    }


}
