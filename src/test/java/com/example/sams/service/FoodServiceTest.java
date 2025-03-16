package com.example.sams.service;

import com.example.sams.entity.Food;
import com.example.sams.mapper.FoodMapper;
import com.example.sams.repo.FoodRepo;
import com.example.sams.request.admin.FoodRequest;
import com.example.sams.response.FoodResponse;
import com.example.sams.service.implementation.FoodService;
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

import java.util.Collections;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FoodServiceTest {
    @Mock
    private FoodRepo foodRepo;

    private final FoodMapper foodMapper=new FoodMapper();

    private FoodService foodService;

    private Validator validator;

    @BeforeEach
    void setUp() {
        foodService=new FoodService(foodRepo,foodMapper);

        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.afterPropertiesSet();
        validator = factoryBean;
    }

    @Test
    void findByNameMatches_ShouldReturnPageOfFoodResponse(){
        // Arrange
        Pageable pageable = mock(Pageable.class);
        Long foodId=1L;
        Food food= Food.builder()
                .foodId(foodId)
                .name("name")
                .producer("producer")
                .servingSize(100f)
                .servingUnits("g")
                .kcal(100)
                .protein(15f)
                .fat(5f)
                .carbs(10f)
                .build();

        Page<Food> foodPage=new PageImpl<>(Collections.singletonList(food));
        when(foodRepo.findByNameContainingIgnoreCase(any(), any())).thenReturn(foodPage);

        // Act
        Page<FoodResponse> responsePage = foodService.findByNameMatches("NaM", pageable);

        // Assert
        assertEquals(1, responsePage.getTotalElements());
        verify(foodRepo).findByNameContainingIgnoreCase("NaM", pageable);
    }

    @Test
    void findByNameMatches_WithNoMatches_ShouldReturnEmptyPage() {
        // Arrange
        Pageable pageable = mock(Pageable.class);
        when(foodRepo.findByNameContainingIgnoreCase("Unknown", pageable)).thenReturn(Page.empty());

        // Act
        Exception exception = assertThrows(RuntimeException.class, () -> {
            foodService.findByNameMatches("Unknown", pageable);
        });


        // Assert
        assertEquals("No food found", exception.getMessage());
    }

    @Test
    void create_WithSameNameAndProducer_ShouldThrowException(){
        // Arrange
        Long foodId=1L;
        Food food= Food.builder()
                .foodId(foodId)
                .name("name")
                .producer("producer")
                .servingSize(100f)
                .servingUnits("g")
                .kcal(100)
                .protein(15f)
                .fat(5f)
                .carbs(10f)
                .build();

        when(foodRepo.findByNameIgnoreCaseAndProducerIgnoreCase("name", "producer")).thenReturn(Optional.of(food));

        FoodRequest request=new FoodRequest(
                food.getName(),
                food.getProducer(),
                food.getServingSize(),
                food.getServingUnits(),
                food.getKcal(),
                food.getProtein(),
                food.getFat(),
                food.getCarbs()
        );
        //Act
        Exception exception = assertThrows(RuntimeException.class, () -> {
            foodService.create(request);
        });

        //Assert
        assertEquals("Food with requested name and producer already exists", exception.getMessage());

    }

    @Test
    void create_valid_shouldReturnFoodResponse(){
        // Arrange
        Long foodId=1L;
        Food food= Food.builder()
                .foodId(foodId)
                .name("name")
                .producer("producer")
                .servingSize(100f)
                .servingUnits("g")
                .kcal(100)
                .protein(15f)
                .fat(5f)
                .carbs(10f)
                .build();

        when(foodRepo.findByNameIgnoreCaseAndProducerIgnoreCase("name", "producer")).thenReturn(Optional.empty());
        when(foodRepo.save(any())).thenReturn(food);

        FoodRequest request=new FoodRequest(
                food.getName(),
                food.getProducer(),
                food.getServingSize(),
                food.getServingUnits(),
                food.getKcal(),
                food.getProtein(),
                food.getFat(),
                food.getCarbs()
        );

        //Act
        FoodResponse response = foodService.create(request);

        //Assert
        assertNotNull(response);
        assertEquals(food.getFoodId(), response.foodId());

    }

}
