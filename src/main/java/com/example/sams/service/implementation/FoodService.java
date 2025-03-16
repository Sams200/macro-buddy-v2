package com.example.sams.service.implementation;

import com.example.sams.entity.Food;
import com.example.sams.mapper.FoodMapper;
import com.example.sams.repo.FoodRepo;
import com.example.sams.request.admin.FoodRequest;
import com.example.sams.response.FoodResponse;
import com.example.sams.service.IFoodService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FoodService implements IFoodService {
    private final FoodRepo foodRepo;
    private final FoodMapper foodMapper;


    @Override
    public Page<FoodResponse> findAll(Pageable pageable) {
        Page<Food> foods = foodRepo.findAll(pageable);
        if(foods.isEmpty())
            throw new RuntimeException("No food found");

        return foods.map(foodMapper::toFoodResponse);
    }

    @Override
    public FoodResponse findById(Long foodId) {
        Optional<Food> food = foodRepo.findById(foodId);
        if(food.isEmpty())
            throw new RuntimeException("No food with requested is could be found");

        return foodMapper.toFoodResponse(food.get());
    }

    @Override
    public Page<FoodResponse> findByNameMatches(String string, Pageable pageable) {
        Page<Food> foods = foodRepo.findByNameContainingIgnoreCase(string, pageable);
        if(foods.isEmpty())
            throw new RuntimeException("No food found");

        return foods.map(foodMapper::toFoodResponse);
    }

    @Override
    public void deleteById(Long foodId) {
        Food food = foodRepo.findById(foodId).orElseThrow(()->new RuntimeException("Food with requested id could not be found"));
        foodRepo.delete(food);
    }

    @Override
    public FoodResponse create(@Valid FoodRequest request){
        Optional<Food> existing=foodRepo.findByNameIgnoreCaseAndProducerIgnoreCase(request.name(), request.producer());
        if (existing.isPresent())
            throw new RuntimeException("Food with requested name and producer already exists");

        Food food=Food.builder()
                .name(request.name())
                .producer(request.producer())
                .servingSize(request.servingSize())
                .servingUnits(request.servingUnits())
                .kcal(request.kcal())
                .protein(request.protein())
                .fat(request.fat())
                .carbs(request.carbs())
                .build();

        Food newFood=foodRepo.save(food);
        return foodMapper.toFoodResponse(newFood);
    }
}
