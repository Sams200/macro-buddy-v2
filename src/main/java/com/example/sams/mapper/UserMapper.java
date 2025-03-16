package com.example.sams.mapper;

import com.example.sams.entity.User;
import com.example.sams.enumeration.Role;
import com.example.sams.response.UserResponse;
import org.springframework.stereotype.Service;

@Service

public class UserMapper {
    public UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole() != null && user.getRole().equals(Role.ROLE_ADMIN)
        );
    }
}
