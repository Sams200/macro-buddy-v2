package com.example.sams.service;

import com.example.sams.entity.User;
import com.example.sams.entity.UserSettings;
import com.example.sams.enumeration.Role;
import com.example.sams.mapper.UserMapper;
import com.example.sams.repo.UserRepo;
import com.example.sams.repo.UserSettingsRepo;
import com.example.sams.request.user.SignInRequest;
import com.example.sams.request.user.SignUpRequest;
import com.example.sams.response.UserResponse;
import com.example.sams.service.implementation.AuthService;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepo userRepo;

    @Mock
    private UserSettingsRepo userSettingsRepo;

    private final UserMapper userMapper=new UserMapper();

    private AuthService authService;

    private Validator validator;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepo, userMapper, userSettingsRepo);

        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.afterPropertiesSet();
        validator = factoryBean;
    }

    @Test
    void signUp_WithValidRequest_ShouldCreateRegularUser(){
        //Mock
        SignUpRequest request = new SignUpRequest(
                "testuser",
                "test@example.com",
                "password123",
                "password123",
                ""
        );

        when(userRepo.findByUsername("testuser")).thenReturn(Optional.empty());
        when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.empty());

        User savedUser = User.builder()
                .userId(1L)
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .role(Role.ROLE_USER)
                .build();

        UserSettings userSettings=UserSettings.builder()
                .goalKcal(2020)
                .goalProtein(170f)
                .goalFat(60f)
                .goalCarbs(200f)
                .user(savedUser)
                .build();

        when(userRepo.save(any(User.class))).thenReturn(savedUser);
        when(userSettingsRepo.save(any(UserSettings.class))).thenReturn(userSettings);

        // Act
        UserResponse response = authService.signUp(request);

        // Assert
        assertNotNull(response);
        assertEquals("testuser", response.username());
        assertEquals("test@example.com", response.email());
        assertEquals(false, response.isAdmin());

        verify(userRepo).findByUsername("testuser");
        verify(userRepo).findByEmail("test@example.com");
        verify(userRepo).save(any(User.class));
    }

    @Test
    void signUp_WithExistingUsername_ShouldThrowException() {
        // Arrange
        SignUpRequest request = new SignUpRequest(
                "sams200",
                "new@example.com",
                "password123",
                "password123",
                ""
        );

        when(userRepo.findByUsername("sams200"))
                .thenReturn(Optional.of(new User()));

        // Act
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.signUp(request);
        });

        //Assert
        assertTrue(exception.getMessage().contains("Username already exists"));
        verify(userRepo).findByUsername("sams200");
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void signUp_WithExistingEmail_ShouldThrowException() {
        // Arrange
        SignUpRequest request = new SignUpRequest(
                "newuser",
                "existing@example.com",
                "password123",
                "password123",
                ""
        );

        when(userRepo.findByEmail("existing@example.com"))
                .thenReturn(Optional.of(new User()));

        // Act
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.signUp(request);
        });

        // Assert
        assertTrue(exception.getMessage().contains("Email is already in use"));
        verify(userRepo).findByEmail("existing@example.com");
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void signUp_WithPasswordMismatch_ShouldThrowException() {
        // Arrange
        SignUpRequest request = new SignUpRequest(
                "newuser",
                "new@example.com",
                "password123",
                "differentpassword",
                ""
        );

        // Act
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.signUp(request);
        });

        // Assert
        assertTrue(exception.getMessage().contains("Passwords do not match"));
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void signUp_WithAdminCode_ShouldCreateAdminUser() {
        // Arrange
        SignUpRequest request = new SignUpRequest(
                "adminuser",
                "admin@example.com",
                "password123",
                "password123",
                "admin"
        );

        when(userRepo.findByUsername("adminuser")).thenReturn(Optional.empty());
        when(userRepo.findByEmail("admin@example.com")).thenReturn(Optional.empty());

        User savedUser = User.builder()
                .userId(1L)
                .username("adminuser")
                .email("admin@example.com")
                .password("password123")
                .role(Role.ROLE_ADMIN)
                .build();

        UserSettings userSettings = UserSettings.builder()
                .goalKcal(2020)
                .goalProtein(170f)
                .goalFat(60f)
                .goalCarbs(200f)
                .user(savedUser)
                .build();

        when(userRepo.save(any(User.class))).thenReturn(savedUser);
        when(userSettingsRepo.save(any(UserSettings.class))).thenReturn(userSettings);

        // Act
        UserResponse response = authService.signUp(request);

        // Assert
        assertNotNull(response);
        assertEquals("adminuser", response.username());
        assertEquals("admin@example.com", response.email());
        assertEquals(true, response.isAdmin());

        verify(userRepo).findByUsername("adminuser");
        verify(userRepo).findByEmail("admin@example.com");
        verify(userRepo).save(any(User.class));
    }

    @Test
    void signUp_WithNullRequest_ShouldThrowException() {
        // Arrange
        SignUpRequest request = null;

        // Act
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.signUp(request);
        });

        // Assert
        assertTrue(exception.getMessage().contains("Sign up request cannot be null"));
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void signIn_WithValidCredentials_ShouldReturnUserResponse() {
        // Arrange
        String username = "testuser";
        String password = "password123";

        User user = User.builder()
                .userId(1L)
                .username(username)
                .email("test@example.com")
                .password(password) // Assume the password is already encoded
                .role(Role.ROLE_USER)
                .build();

        when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));

        SignInRequest request = new SignInRequest(username, password);
        // Act
        UserResponse response = authService.signIn(request);

        // Assert
        assertNotNull(response);
        assertEquals(username, response.username());
        assertEquals(user.getEmail(), response.email());
        assertEquals(false, response.isAdmin());
    }

    @Test
    void signIn_WithInvalidUsername_ShouldThrowException() {
        // Arrange
        String username = "nonexistentuser";
        String password = "password123";

        when(userRepo.findByUsername(username)).thenReturn(Optional.empty());
        SignInRequest request = new SignInRequest(username, password);
        // Act
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.signIn(request);
        });

        // Assert
        assertTrue(exception.getMessage().contains("User not found"));
    }

    @Test
    void signIn_WithEmptyUsername_ShouldThrowException() {
        // Arrange
        String username = "";
        String password = "password123";
        SignInRequest request = new SignInRequest(username, password);
        // Act
        var valids=validator.validate(request);

        // Assert
        assertFalse(valids.isEmpty());
        assertEquals("The username is required", valids.iterator().next().getMessage());
    }
}
