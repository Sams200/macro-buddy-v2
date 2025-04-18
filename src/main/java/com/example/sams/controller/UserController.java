package com.example.sams.controller;

import com.example.sams.request.user.PasswordChangeRequest;
import com.example.sams.response.HttpResponse;
import com.example.sams.response.UserResponse;
import com.example.sams.service.implementation.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<HttpResponse> findAll(Pageable pageable){
        Page<UserResponse> res=userService.findAll(pageable);

        return ResponseEntity.ok(
            HttpResponse
                .builder()
                .timestamp(LocalDateTime.now().toString())
                .responseStatusCode(HttpStatus.OK.value())
                .responseStatus(HttpStatus.OK)
                .responseMessage("Users found successfully")
                .body(res)
                .build()
        );
    }

    @GetMapping("/id={userId}")
    public ResponseEntity<HttpResponse> findById(@PathVariable("userId") Long userId){
        UserResponse res=userService.findById(userId);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseStatusCode(HttpStatus.OK.value())
                        .responseStatus(HttpStatus.OK)
                        .responseMessage("User found successfully")
                        .body(res)
                        .build()
        );
    }

    @PatchMapping("/authenticated/change-password")
    public ResponseEntity<HttpResponse> changePassword(Authentication authentication,  @RequestBody @Valid PasswordChangeRequest request){
        userService.changePassword(authentication,request);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseStatusCode(HttpStatus.OK.value())
                        .responseStatus(HttpStatus.OK)
                        .responseMessage("Password changed successfully")
                        .build()
        );
    }

    @DeleteMapping("/id={userId}")
    public ResponseEntity<HttpResponse> delete(@PathVariable("userId") Long userId){
        userService.deleteById(userId);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseStatusCode(HttpStatus.OK.value())
                        .responseStatus(HttpStatus.OK)
                        .responseMessage("User deleted successfully")
                        .build()
        );
    }

    @GetMapping("/authenticated")
    public ResponseEntity<HttpResponse> findAuthenticatedUserData(Authentication authentication) {
        var result = userService.findAuthenticatedUserData(authentication);

        return ResponseEntity.ok(
                HttpResponse
                        .builder()
                        .timestamp(LocalDateTime.now().toString())
                        .responseMessage("The user has been found successfully")
                        .responseStatus(HttpStatus.OK)
                        .responseStatusCode(HttpStatus.OK.value())
                        .body(result)
                        .build()
        );
    }
}
