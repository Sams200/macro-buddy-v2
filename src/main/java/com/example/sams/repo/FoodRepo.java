package com.example.sams.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.sams.entity.Food;

import java.util.Optional;

@Repository
public interface FoodRepo extends JpaRepository<Food, Long> {
    Page<Food> findByName(String name, Pageable pageable);
    Page<Food> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Food> findByProducer(String producer, Pageable pageable);

    Optional<Food> findByNameIgnoreCaseAndProducerIgnoreCase(String name, String producer);
}
