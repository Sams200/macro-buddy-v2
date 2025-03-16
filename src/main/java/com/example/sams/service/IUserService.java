package com.example.sams.service;

import com.example.sams.entity.User;
import com.example.sams.request.user.PasswordChangeRequest;
import com.example.sams.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public interface IUserService {
    Page<UserResponse> findAll(Pageable pageable);
    UserResponse findById(Long id);


    void changePassword(Long userId, PasswordChangeRequest request);
    void deleteById(Long id);
}
