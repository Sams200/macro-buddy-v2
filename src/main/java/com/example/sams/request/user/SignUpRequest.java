package com.example.sams.request.user;

import com.example.sams.validation.StringMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@StringMatch(
        first = "password",
        second = "confirmPassword",
        message = "The password confirmation does not match"
)
public record SignUpRequest(
        @NotEmpty(message = "The username is required")
        @Size(min = 5, max = 30, message = "The username must contain at least 5 and at most 30 characters")
        String username,

        @NotEmpty(message = "The email is required")
        @Email(message = "The email must follow the 'email@domain.com' pattern")
        @Size(max = 50, message = "The email must contain at most 50 characters")
        String email,

        @NotEmpty(message = "The password is required")
        @Size(min = 5, max = 40, message = "The password must contain at least 5 and at most 40 characters")
        String password,

        @NotEmpty(message = "The password confirmation is required")
        @Size(min = 5, max = 40, message = "The password confirmation must contain at least 5 and at most 40 characters")
        String confirmPassword,

        @NotEmpty(message = "The recaptcha is required")
        String recaptchaToken
) {
}
