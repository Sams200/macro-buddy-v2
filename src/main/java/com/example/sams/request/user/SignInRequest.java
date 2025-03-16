package com.example.sams.request.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record SignInRequest(
        @NotEmpty(message = "The username is required")
        @Size(min = 5, max = 30, message = "The username must contain at least 5 and at most 30 characters")
        String username,

        @NotEmpty(message = "The password is required")
        @Size(min = 5, max = 40, message = "The password must contain at least 5 and at most 40 characters")
        String password
) {
}
