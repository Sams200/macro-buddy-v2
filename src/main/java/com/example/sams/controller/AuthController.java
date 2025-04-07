package com.example.sams.controller;

import com.example.sams.request.user.SignInRequest;
import com.example.sams.request.user.SignUpRequest;
import com.example.sams.response.HttpResponse;
import com.example.sams.response.UserResponse;
import com.example.sams.service.implementation.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authentication")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<HttpResponse> signUp(@RequestBody SignUpRequest request) {
        UserResponse res = authService.signUp(request);

        return ResponseEntity.created(URI.create("")).body(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseStatusCode(HttpStatus.CREATED.value())
                        .responseStatus(HttpStatus.CREATED)
                        .responseMessage("User created successfully")
                        .body(res)
                        .build()
        );
    }

    @PostMapping("/sign-in")
    public ResponseEntity<HttpResponse> signIn(@RequestBody SignInRequest request, HttpServletResponse response) {
        authService.signIn(request,response);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("Sign in successful for " + request.username())
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/sign-out")
    public ResponseEntity<HttpResponse> signOut(HttpServletResponse response) {
        authService.signOut(response);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("Sign out successful")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .build()
        );
    }
}
