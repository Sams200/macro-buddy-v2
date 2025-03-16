package com.example.sams.service;

import com.example.sams.entity.Food;
import com.example.sams.request.admin.FoodRequest;
import com.example.sams.response.FoodResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface IFoodService {
    Page<FoodResponse> findAll(Pageable pageable);
    FoodResponse findById(Long foodId);
    FoodResponse create(FoodRequest request);
    void deleteById(Long foodId);
    Page<FoodResponse> findByNameMatches(String string, Pageable pageable);
}
