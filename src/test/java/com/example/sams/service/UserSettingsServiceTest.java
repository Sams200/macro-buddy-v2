package com.example.sams.service;

import com.example.sams.entity.User;
import com.example.sams.entity.UserSettings;
import com.example.sams.mapper.UserMapper;
import com.example.sams.mapper.UserSettingsMapper;
import com.example.sams.repo.EntryRepo;
import com.example.sams.repo.UserRepo;
import com.example.sams.repo.UserSettingsRepo;
import com.example.sams.request.user.UserSettingsRequest;
import com.example.sams.response.UserSettingsResponse;
import com.example.sams.service.implementation.UserService;
import com.example.sams.service.implementation.UserSettingsService;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserSettingsServiceTest {
    @Mock
    private UserRepo userRepo;
    @Mock
    private UserSettingsRepo userSettingsRepo;
    @Mock
    private EntryRepo entryRepo;

    private final UserMapper userMapper = new UserMapper();
    private final UserSettingsMapper userSettingsMapper=new UserSettingsMapper();

    private UserSettingsService userSettingsService;
    private UserService userService;

    @Mock
    private Validator validator;

    private User user;
    private UserSettings userSettings;
    private UserSettingsResponse userSettingsResponse;

    @BeforeEach
    void setUp() {
        userService=new UserService(userRepo,entryRepo,userMapper);
        userSettingsService=new UserSettingsService(userRepo,userSettingsRepo,userSettingsMapper);

        user = User.builder()
                .userId(1L)
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .build();

        userSettings = UserSettings.builder()
                .userSettingsId(1L)
                .goalKcal(2000)
                .goalProtein(150f)
                .goalFat(50f)
                .goalCarbs(200f)
                .user(user)
                .build();

        userSettingsResponse = new UserSettingsResponse(
                2000,
                170f,
                60f,
                200f

        );
    }

    @Test
    void findByUserId_WithValidUserId_ShouldReturnUserSettingsResponse() {
        // Arrange
        when(userSettingsRepo.findByUser_UserId(1L)).thenReturn(Optional.of(userSettings));

        // Act
        UserSettingsResponse response = userSettingsService.findByUserId(1L);

        // Assert
        assertNotNull(response);
        assertEquals(2000, response.dailyKcalGoal());
        assertEquals(150f, response.dailyProteinGoal());
        assertEquals(50f, response.dailyFatGoal());
        assertEquals(200f, response.dailyCarbGoal());
    }

    @Test
    void findByUserId_WithInvalidUserId_ShouldThrowException() {
        // Arrange
        when(userSettingsRepo.findByUser_UserId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userSettingsService.findByUserId(999L);
        });

        assertEquals("User settings not found", exception.getMessage());
    }

    @Test
    void createOrUpdate_WithNewSettings_ShouldCreateUserSettings() {
        // Arrange
        UserSettingsRequest request = new UserSettingsRequest(2000, 150f, 50f, 200f);

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(userSettingsRepo.findByUser_UserId(1L)).thenReturn(Optional.empty());
        when(userSettingsRepo.save(any(UserSettings.class))).thenReturn(userSettings);

        // Act
        UserSettingsResponse response = userSettingsService.createOrUpdate(1L, request);

        // Assert
        assertNotNull(response);
        assertEquals(2000, response.dailyKcalGoal());
        assertEquals(150f, response.dailyProteinGoal());
        assertEquals(50f, response.dailyFatGoal());
        assertEquals(200f, response.dailyCarbGoal());
    }

    @Test
    void createOrUpdate_WithExistingSettings_ShouldUpdateUserSettings() {
        // Arrange
        UserSettingsRequest request = new UserSettingsRequest(2500, 160f, 60f, 250f);

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(userSettingsRepo.findByUser_UserId(1L)).thenReturn(Optional.of(userSettings));
        when(userSettingsRepo.save(any(UserSettings.class))).thenReturn(userSettings);

        // Act
        UserSettingsResponse response = userSettingsService.createOrUpdate(1L, request);

        // Assert
        assertNotNull(response);
        assertEquals(2500, response.dailyKcalGoal());
        assertEquals(160f, response.dailyProteinGoal());
        assertEquals(60f, response.dailyFatGoal());
        assertEquals(250f, response.dailyCarbGoal());
    }

    @Test
    void createOrUpdate_WithInvalidUserId_ShouldThrowException() {
        // Arrange
        UserSettingsRequest request = new UserSettingsRequest(2000, 150f, 50f, 200f);
        when(userRepo.findById(999L)).thenReturn(Optional.empty());

        // Act
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            userSettingsService.createOrUpdate(999L, request);
        });

        // Assert
        assertEquals("User not found: 999", exception.getMessage());
        verify(userRepo).findById(999L);
        verify(userSettingsRepo, never()).save(any());
    }

    @Test
    void createOrUpdate_WithNullRequest_ShouldThrowException() {
        // Arrange
        UserSettingsRequest request = null;

        // Act
        assertThrows(EntityNotFoundException.class, () -> {
            userSettingsService.createOrUpdate(1L, request);
        });

        // Assert
        verify(userSettingsRepo, never()).save(any());
    }
}
