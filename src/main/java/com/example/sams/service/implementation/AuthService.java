package com.example.sams.service.implementation;

import com.example.sams.controller.SessionManager;
import com.example.sams.entity.User;
import com.example.sams.entity.UserSettings;
import com.example.sams.enumeration.Role;
import com.example.sams.mapper.UserMapper;
import com.example.sams.repo.UserRepo;
import com.example.sams.repo.UserSettingsRepo;
import com.example.sams.request.user.SignInRequest;
import com.example.sams.request.user.SignUpRequest;
import com.example.sams.response.UserResponse;
import com.example.sams.service.IAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    //private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final UserSettingsRepo userSettingsRepo;


    @Override
    public UserResponse signUp(@Valid SignUpRequest request) {
        if(request == null)
            throw new IllegalArgumentException("Sign up request cannot be null");

        if(!request.password().equals(request.confirmPassword()))
            throw new IllegalArgumentException("Passwords do not match");

        String name=request.username();
        String mail=request.email();

        Optional<User> existing=userRepo.findByUsername(name);
        if(existing.isPresent()){
            throw new RuntimeException("Username already exists");
        }

        existing=userRepo.findByEmail(mail);
        if(existing.isPresent()){
            throw new RuntimeException("Email is already in use");
        }

        User user=User.builder()
                .username(name)
                .email(mail)
                .password(request.password())
                .role(Role.ROLE_USER)
                .build();

        if(request.adminPassword().equals("admin"))
            user.setRole(Role.ROLE_ADMIN);

        user=userRepo.save(user);

        UserSettings userSettings=UserSettings.builder()
                .goalKcal(2020)
                .goalProtein(170f)
                .goalFat(60f)
                .goalCarbs(200f)
                .user(user)
                .build();

        userSettingsRepo.save(userSettings);

        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse signIn(@Valid SignInRequest signInRequest) {
        if(signInRequest == null)
            throw new IllegalArgumentException("Sign in request cannot be null");

        User user=authenticate(signInRequest.username(), signInRequest.password());
        SessionManager.setUser(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    public void signOut() {
        SessionManager.logout();
    }

    private User authenticate(String username, String password) {
        User user = userRepo.findByUsername(username).orElseThrow(()->new RuntimeException("User not found"));
        if(! user.getPassword().equals(password)){
            throw new RuntimeException("Wrong password");
        }

        return user;
    }
}
