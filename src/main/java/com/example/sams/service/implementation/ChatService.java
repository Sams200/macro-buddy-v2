package com.example.sams.service.implementation;

import com.example.sams.entity.User;
import com.example.sams.enumeration.ChatMessageType;
import com.example.sams.request.admin.ChatMessageRequest;
import com.example.sams.response.ChatMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {
    public ChatMessageResponse connectToPublicChat(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return new ChatMessageResponse(
                user.getUserId(),
                user.getUsername(),
                ChatMessageType.CONNECT,
                user.getUsername() + " connected.",
                LocalDateTime.now());
    }

    public ChatMessageResponse disconnectFromPublicChat(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return new ChatMessageResponse(
                user.getUserId(),
                user.getUsername(),
                ChatMessageType.CONNECT,
                user.getUsername() + " disconnected.",
                LocalDateTime.now());
    }

    public ChatMessageResponse sendPublicMessage(Authentication authentication, ChatMessageRequest request) {
        User user = (User) authentication.getPrincipal();

        return new ChatMessageResponse(
                user.getUserId(),
                user.getUsername(),
                ChatMessageType.MESSAGE,
                request.content(),
                LocalDateTime.now());
    }
}
