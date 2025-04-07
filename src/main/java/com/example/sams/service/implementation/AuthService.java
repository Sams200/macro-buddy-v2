package com.example.sams.service.implementation;

import com.example.sams.entity.User;
import com.example.sams.entity.UserSettings;
import com.example.sams.enumeration.Role;
import com.example.sams.exception.ResourceAlreadyExistsException;
import com.example.sams.exception.ResourceNotFoundException;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final UserSettingsRepo userSettingsRepo;
    //private final CaptchaService captchaService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CookieService cookieService;

    @Value("${jwt.duration}")
    private String jwtDuration;


    @Override
    public UserResponse signUp(@Valid SignUpRequest request) {
        //captchaService.validateTokenV2(request.recaptchaToken());

        String name=request.username();
        String mail=request.email();

        Optional<User> existing=userRepo.findByUsername(name);
        if(existing.isPresent()){
            throw new ResourceAlreadyExistsException("Username already exists");
        }

        existing=userRepo.findByEmail(mail);
        if(existing.isPresent()){
            throw new ResourceAlreadyExistsException("Email is already in use");
        }

        User user=User.builder()
                .username(name)
                .email(mail)
                .password(passwordEncoder.encode(request.password()))
                .role(Role.ROLE_USER)
                .build();



        UserSettings userSettings=UserSettings.builder()
                .goalKcal(2020)
                .goalProtein(170f)
                .goalFat(60f)
                .goalCarbs(200f)
                .user(user)
                .build();

        user=userRepo.save(user);
        userSettingsRepo.save(userSettings);

        return userMapper.toUserResponse(user);
    }

    @Override
    public void signIn(@Valid SignInRequest signInRequest,HttpServletResponse response) {
        User user=authenticate(signInRequest.username(), signInRequest.password());
        String jwt= jwtService.generateToken(user, Long.parseLong(jwtDuration));
        HttpCookie cookie= cookieService.createAccessTokenCookie(jwt, Long.parseLong(jwtDuration));

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    @Override
    public void signOut(HttpServletResponse response) {
        HttpCookie cookie1 = cookieService.deleteAccessTokenCookie();
        HttpCookie cookie2 = cookieService.removeXsrfCookie();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie1.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, cookie2.toString());
    }

    private User authenticate(String username, String password) {
        User user = userRepo.findByUsername(username).orElseThrow(()->new ResourceNotFoundException("User not found"));
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        return user;
    }
}
