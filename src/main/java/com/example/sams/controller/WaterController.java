package com.example.sams.controller;

import com.example.sams.request.user.EntryRequest;
import com.example.sams.request.user.WaterRequest;
import com.example.sams.response.EntryResponse;
import com.example.sams.response.HttpResponse;
import com.example.sams.response.WaterResponse;
import com.example.sams.service.implementation.WaterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authenticated/water")
public class WaterController {
    private final WaterService waterService;

    @GetMapping
    public ResponseEntity<HttpResponse> findByAuthenticatedUser(Authentication authentication, Pageable pageable) {
        Page<WaterResponse> result = waterService.findByUser(authentication,pageable);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The waters have been found successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }

    @GetMapping("/date={waterDate}")
    public ResponseEntity<HttpResponse> findByAuthenticatedUserAndDate(@PathVariable("waterDate") LocalDate waterDate, Authentication authentication) {
        WaterResponse result = waterService.findByUserAndDate(authentication,waterDate);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The waters have been found successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<HttpResponse> save(Authentication authentication, @RequestBody @Valid WaterRequest request) {
        WaterResponse result = waterService.save(authentication, request);

        return ResponseEntity.created(URI.create("")).body(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The entry has been saved successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }

    @DeleteMapping("/id={waterId}")
    public ResponseEntity<HttpResponse> deleteById(Authentication authentication,@PathVariable("waterId") Long waterId) {
        waterService.deleteById(authentication,waterId);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The water has been deleted successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PatchMapping("/id={waterId}")
    public ResponseEntity<HttpResponse> updateById(Authentication authentication,@PathVariable("waterId") Long waterId, @RequestBody @Valid WaterRequest request) {
        var result = waterService.update(authentication,request,waterId);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The water has been updated successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }
}
