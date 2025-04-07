package com.example.sams.service;

import com.example.sams.entity.UserSettings;
import com.example.sams.request.user.UserSettingsRequest;
import com.example.sams.response.UserSettingsResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface IUserSettingsService {
    UserSettingsResponse findAuthenticatedUserData(Authentication authentication);
    UserSettingsResponse update(Authentication authentication, UserSettingsRequest request);
}
