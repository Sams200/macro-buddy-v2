package com.example.sams.response;

import com.example.sams.enumeration.ChatMessageType;

import java.time.LocalDateTime;

public record ChatMessageResponse(
        Long userId,
        String username,
        ChatMessageType messageType,
        String content,
        LocalDateTime createdDate
) {
}
