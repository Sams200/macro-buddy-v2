package com.example.sams.controller;

import com.example.sams.request.user.UserSettingsRequest;
import com.example.sams.response.HttpResponse;
import com.example.sams.service.implementation.UserSettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authenticated/user-settings")
public class UserSettingsController {
    private final UserSettingsService userSettingsService;


    @GetMapping
    public ResponseEntity<HttpResponse> findAuthenticatedUserData(Authentication authentication) {
        var result = userSettingsService.findAuthenticatedUserData(authentication);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The user settings have been found successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }

    @PatchMapping
    public ResponseEntity<HttpResponse> update(Authentication authentication, @RequestBody @Valid UserSettingsRequest request) {
        var result = userSettingsService.update(authentication, request);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The user settings have been updated successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }
}
