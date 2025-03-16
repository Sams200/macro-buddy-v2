package com.example.sams.service;

import com.example.sams.entity.UserSettings;
import com.example.sams.request.user.UserSettingsRequest;
import com.example.sams.response.UserSettingsResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface IUserSettingsService {
    UserSettingsResponse findByUserId(Long userId);
    UserSettingsResponse createOrUpdate(Long userId, UserSettingsRequest request);
}
