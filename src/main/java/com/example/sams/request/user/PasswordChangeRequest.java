package com.example.sams.request.user;

import com.example.sams.validation.StringMatch;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@StringMatch(
        first="newPassword",
        second="confirmNewPassword",
        message="The password confirmation does not match"
)
public record PasswordChangeRequest(
        @NotEmpty(message = "The password is required")
        @Size(min = 5, max = 40, message = "The password must contain at least 5 and at most 40 characters")
        String currentPassword,

        @NotEmpty(message = "The new password is required")
        @Size(min = 5, max = 40, message = "The password must contain at least 5 and at most 40 characters")
        String newPassword,

        @NotEmpty(message = "The new password confirmation is required")
        @Size(min = 5, max = 40, message = "The password must contain at least 5 and at most 40 characters")
        String confirmNewPassword
) {
}

