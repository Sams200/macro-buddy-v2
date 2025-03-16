package com.example.sams.service;

import com.example.sams.entity.Entry;
import com.example.sams.entity.Food;
import com.example.sams.entity.User;
import com.example.sams.enumeration.MealType;
import com.example.sams.mapper.EntryMapper;
import com.example.sams.repo.EntryRepo;
import com.example.sams.repo.FoodRepo;
import com.example.sams.repo.UserRepo;
import com.example.sams.request.user.EntryRequest;
import com.example.sams.response.EntryResponse;
import com.example.sams.service.implementation.EntryService;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EntryServiceTest {
    @Mock
    private UserRepo userRepo;

    @Mock
    private EntryRepo entryRepo;

    @Mock
    private FoodRepo foodRepo;

    private final EntryMapper entryMapper=new EntryMapper();

    private EntryService entryService;

    private Validator validator;

    @BeforeEach
    void setUp() {
        entryService=new EntryService(userRepo,entryRepo,entryMapper,foodRepo);

        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.afterPropertiesSet();
        validator = factoryBean;
    }

    @Test
    void findByUser_ShouldReturnPageOfEntryResponses() {
        // Arrange
        Long userId = 1L;
        Pageable pageable = mock(Pageable.class);
        User user = new User();
        user.setUserId(userId);

        Entry entry = Entry.builder()
                .entryId(1L)
                .quantity(100f)
                .meal(MealType.BREAKFAST)
                .date(LocalDate.now())
                .food(new Food())
                .user(user)
                .build();

        List<Entry> entryPage = new ArrayList<>(Collections.singletonList(entry));
        when(entryRepo.findByUser_UserId(userId)).thenReturn(entryPage);

        // Act
        List<EntryResponse> responsePage = entryService.findByUser(userId);

        // Assert
        assertEquals(1, responsePage.size());
        verify(entryRepo).findByUser_UserId(userId);
    }

    @Test
    void deleteById_WithValidId_ShouldDeleteEntry() {
        // Arrange
        Long entryId = 1L;
        Entry entry = new Entry();
        entry.setEntryId(entryId);

        when(entryRepo.findById(entryId)).thenReturn(Optional.of(entry));

        // Act
        entryService.deleteById(entryId);

        // Assert
        verify(entryRepo).delete(entry);
    }

    @Test
    void deleteById_WithInvalidId_ShouldThrowException() {
        // Arrange
        Long entryId = 999L;
        when(entryRepo.findById(entryId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            entryService.deleteById(entryId);
        });

        assertEquals("Entry not found with id", exception.getMessage());
        verify(entryRepo, never()).delete(any());
    }

    @Test
    void create_WithValidRequest_ShouldReturnEntryResponse() {
        // Arrange
        Long userId = 1L;
        Long foodId = 1L;
        User user = new User();
        user.setUserId(1L);

        Food food = new Food();
        food.setFoodId(1L);

        EntryRequest request = new EntryRequest(LocalDate.now(), "BREAKFAST", 2f, foodId);

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(foodRepo.findById(foodId)).thenReturn(Optional.of(food));

        Entry savedEntry = Entry.builder()
                .entryId(1L)
                .quantity(request.quantity())
                .meal(MealType.BREAKFAST)
                .date(request.date())
                .food(food)
                .user(user)
                .build();

        when(entryRepo.save(any(Entry.class))).thenReturn(savedEntry);

        // Act
        EntryResponse response = entryService.create(userId, request);

        // Assert
        assertNotNull(response);
        assertEquals(savedEntry.getEntryId(), response.entryId());
        verify(userRepo).findById(userId);
        verify(foodRepo).findById(foodId);
        verify(entryRepo).save(any(Entry.class));
    }

    @Test
    void create_WithInvalidFoodId_ShouldThrowException() {
        // Arrange
        Long userId = 1L;
        Long foodId = 999L;
        User user = new User();
        user.setUserId(userId);

        EntryRequest request = new EntryRequest(LocalDate.now(), "BREAKFAST", 2f, foodId);

        when(foodRepo.findById(foodId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            entryService.create(userId, request);
        });

        assertEquals("Food not found with id", exception.getMessage());
        verify(entryRepo, never()).save(any());
    }

    @Test
    void create_WithInvalidMealType_ShouldFailValidation() {
        // Arrange
        EntryRequest request = new EntryRequest(
                LocalDate.now(),
                "INVALID_MEAL", // Invalid meal type
                100f,
                1L
        );

        // Act
        var violations = validator.validate(request);

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals("Invalid meal type", violations.iterator().next().getMessage());
    }
}
