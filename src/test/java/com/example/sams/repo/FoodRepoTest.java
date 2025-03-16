package com.example.sams.repo;

import com.example.sams.entity.Food;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class FoodRepoTest {
    @Autowired
    private FoodRepo foodRepo;

    private Food food1;
    private Food food2;

    @BeforeEach
    void setUp() {
        food1 = Food.builder()
                .name("TestFood1")
                .producer("Mega")
                .servingSize(100f)
                .servingUnits("g")
                .kcal(265)
                .build();

        food2 = Food.builder()
                .name("DifferentFood")
                .producer("Mega")
                .servingSize(100f)
                .servingUnits("g")
                .kcal(245)
                .build();

    }
    @Test
    void findByNameContainingIgnoreCase_ShouldReturnMatchingFoods() {
        // Arrange
        foodRepo.save(food1);
        foodRepo.save(food2);

        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<Food> result = foodRepo.findByNameContainingIgnoreCase("test", pageable);
        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("TestFood1", result.getContent().get(0).getName());
    }

    @Test
    void findByNameIgnoreCaseAndProducerIgnoreCase_ShouldReturnMatchingFoods() {
        // Arrange
        foodRepo.save(food1);

        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Optional<Food> result = foodRepo.findByNameIgnoreCaseAndProducerIgnoreCase(food1.getName(),food1.getProducer());
        // Assert
        assertTrue(result.isPresent());
        assertEquals("TestFood1",result.get().getName());
        assertEquals("Mega",result.get().getProducer());
    }
}
