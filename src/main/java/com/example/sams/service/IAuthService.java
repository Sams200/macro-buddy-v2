package com.example.sams.service;

import com.example.sams.request.user.SignInRequest;
import com.example.sams.request.user.SignUpRequest;
import com.example.sams.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public interface IAuthService {
    UserResponse signUp(SignUpRequest signUpRequest);
    public void signIn(@Valid SignInRequest signInRequest, HttpServletResponse response);
    public void signOut(HttpServletResponse response);
}
