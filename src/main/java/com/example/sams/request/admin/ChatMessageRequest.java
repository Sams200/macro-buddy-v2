package com.example.sams.request.admin;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record ChatMessageRequest(
        @NotEmpty(message = "The message is required")
        @Size(max = 256, message = "The message must contain at most 256 characters")
        String content
) {
}
