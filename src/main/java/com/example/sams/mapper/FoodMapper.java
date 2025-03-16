package com.example.sams.mapper;

import com.example.sams.entity.Food;
import com.example.sams.response.FoodResponse;
import org.springframework.stereotype.Service;

@Service
public class FoodMapper {
    public FoodResponse toFoodResponse(Food food){
        return new FoodResponse(
                food.getFoodId(),
                food.getName(),
                food.getProducer(),
                food.getServingSize(),
                food.getServingUnits(),
                food.getKcal(),
                food.getProtein(),
                food.getFat(),
                food.getCarbs()
        );
    }
}
