package com.example.sams.response;

public record UserResponse(
        Long userId,
        String username,
        String email,
        Boolean isAdmin
) {
}
