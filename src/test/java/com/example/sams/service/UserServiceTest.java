package com.example.sams.service;

import com.example.sams.entity.Entry;
import com.example.sams.entity.User;
import com.example.sams.enumeration.Role;
import com.example.sams.mapper.UserMapper;
import com.example.sams.repo.EntryRepo;
import com.example.sams.repo.UserRepo;
import com.example.sams.request.user.PasswordChangeRequest;
import com.example.sams.response.UserResponse;
import com.example.sams.service.implementation.UserService;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepo userRepo;
    private final UserMapper userMapper=new UserMapper();
    @Mock
    private EntryRepo entryRepo;
    private UserService userService;
    private Validator validator;

    private User user;
    private UserResponse userResponse;
    @BeforeEach
    void setUp() {
        userService=new UserService(userRepo,entryRepo,userMapper);

        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.afterPropertiesSet();
        validator = factoryBean;

        user = User.builder()
                .userId(1L)
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .role(Role.ROLE_USER)
                .build();

        userResponse = new UserResponse(1L, "testuser", "test@example.com", false);
    }

    @Test
    void findAll_ShouldReturnPageOfUserResponses() {
        // Arrange
        Pageable pageable = mock(Pageable.class);
        Page<User> userPage = new PageImpl<>(Collections.singletonList(user));
        when(userRepo.findAll(pageable)).thenReturn(userPage);

        // Act
        Page<UserResponse> responsePage = userService.findAll(pageable);

        // Assert
        assertEquals(1, responsePage.getTotalElements());
    }

    @Test
    void findAll_WithNoUsers_ShouldThrowException() {
        // Arrange
        Pageable pageable = mock(Pageable.class);
        when(userRepo.findAll(pageable)).thenReturn(Page.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.findAll(pageable);
        });

        assertEquals("Users not found", exception.getMessage());
    }

    @Test
    void findById_WithValidId_ShouldReturnUserResponse() {
        // Arrange
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        // Act
        UserResponse response = userService.findById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.userId());
    }

    @Test
    void findById_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(userRepo.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.findById(999L);
        });

        assertEquals("User with requested id not found", exception.getMessage());
    }

    @Test
    void changePassword_WithValidRequest_ShouldUpdatePassword() {
        // Arrange
        PasswordChangeRequest request = new PasswordChangeRequest("password123", "newpassword123","newpassword123");
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        when(userRepo.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            user.setPassword(savedUser.getPassword());
            return savedUser;
        });

        // Act
        userService.changePassword(1L, request);

        // Assert
        assertEquals("newpassword123", user.getPassword());
    }

    @Test
    void changePassword_WithInvalidUserId_ShouldThrowException() {
        // Arrange
        PasswordChangeRequest request = new PasswordChangeRequest("password123", "newpassword123","newpassword123");
        when(userRepo.findById(999L)).thenReturn(Optional.empty());

        // Act
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.changePassword(999L, request);
        });

        // Assert
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void changePassword_WithIncorrectCurrentPassword_ShouldThrowException() {
        // Arrange
        PasswordChangeRequest request = new PasswordChangeRequest("wrongpassword", "newpassword123","newpassword123");
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        // Act
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.changePassword(1L, request);
        });

        // Assert
        assertEquals("Current password does not match", exception.getMessage());
    }

    @Test
    void deleteById_WithValidId_ShouldDeleteUserAndEntries() {
        // Arrange
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        List<Entry> entries = new ArrayList<>(Collections.emptyList());
        when(entryRepo.findByUser_UserId(1L)).thenReturn(entries);

        // Act
        userService.deleteById(1L);

        // Assert
        verify(userRepo).findById(1L);
        verify(entryRepo).findByUser_UserId(1L);
        verify(entryRepo).deleteAll(entries);
        verify(userRepo).delete(user);
    }

    @Test
    void deleteById_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(userRepo.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.deleteById(999L);
        });

        assertEquals("User not found", exception.getMessage());
        verify(entryRepo, never()).deleteAll(any());
        verify(userRepo, never()).delete(any());
    }
}
