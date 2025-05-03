package com.example.sams.controller;

import com.example.sams.entity.User;
import com.example.sams.request.admin.ChatMessageRequest;
import com.example.sams.response.ChatMessageResponse;
import com.example.sams.service.implementation.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authenticated/chat")
public class ChatController {
    public final ChatService chatService;
    private final SimpMessagingTemplate template;

    @PostMapping("/connect-to-public-chat")
    public ChatMessageResponse connectToPublicChat(Authentication authentication) {
        var result = chatService.connectToPublicChat(authentication);

        template.convertAndSend("/topic/public", result);

        return result;
    }

    @PostMapping("/disconnect-from-public-chat")
    public ChatMessageResponse disconnectFromPublicChat(Authentication authentication) {
        var result = chatService.disconnectFromPublicChat(authentication);

        template.convertAndSend("/topic/public", result);

        return result;
    }

    @PostMapping("/send-public-message")
    public ChatMessageResponse sendPublicMessage(Authentication authentication, @RequestBody @Valid ChatMessageRequest request) {
        var result = chatService.sendPublicMessage(authentication, request);

        template.convertAndSend("/topic/public", result);

        return result;
    }
}
