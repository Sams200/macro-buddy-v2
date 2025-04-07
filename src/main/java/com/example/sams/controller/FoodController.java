package com.example.sams.controller;

import com.example.sams.request.admin.FoodRequest;
import com.example.sams.response.FoodResponse;
import com.example.sams.response.HttpResponse;
import com.example.sams.service.implementation.FoodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/food")
public class FoodController {
    private final FoodService foodService;

    @GetMapping
    public ResponseEntity<HttpResponse> findAll(Pageable pageable) {
        Page<FoodResponse> result = foodService.findAll(pageable);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The foods have been found successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }

    @GetMapping("/id={foodId}")
    public ResponseEntity<HttpResponse> findById(@PathVariable("foodId") Long foodId) {
        FoodResponse result = foodService.findById(foodId);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The food has been found successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }

    @GetMapping("/name={foodName}")
    public ResponseEntity<HttpResponse> findByNameMatches(@PathVariable("foodName") String foodName, Pageable pageable) {
        Page<FoodResponse> result = foodService.findByNameMatches(foodName, pageable);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The foods have been found successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }

    @DeleteMapping("/id={foodId}")
    public ResponseEntity<HttpResponse> deleteById(@PathVariable("foodId") Long foodId) {
        foodService.deleteById(foodId);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The food has been deleted successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpResponse> save(@ModelAttribute @Valid FoodRequest foodRequest) {
        FoodResponse result = foodService.create(foodRequest);

        return ResponseEntity.created(URI.create("")).body(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The food has been saved successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }
}
