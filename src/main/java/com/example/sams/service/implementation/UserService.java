package com.example.sams.service.implementation;

import com.example.sams.entity.Entry;
import com.example.sams.entity.User;
import com.example.sams.entity.UserSettings;
import com.example.sams.exception.ResourceNotFoundException;
import com.example.sams.mapper.UserMapper;
import com.example.sams.repo.EntryRepo;
import com.example.sams.repo.UserRepo;
import com.example.sams.repo.UserSettingsRepo;
import com.example.sams.request.user.PasswordChangeRequest;
import com.example.sams.response.UserResponse;
import com.example.sams.service.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepo userRepo;
    private final EntryRepo entryRepo;
    private final UserMapper userMapper;
    private final UserSettingsRepo userSettingsRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UserResponse> findAll(Pageable pageable) {
        Page<User> users = userRepo.findAll(pageable);
        if(users.isEmpty()){
            throw new ResourceNotFoundException("Users not found");
        }

        return users.map(userMapper::toUserResponse);
    }

    @Override
    public UserResponse findById(Long id) {
        Optional<User> user = userRepo.findById(id);
        if(user.isEmpty()){
            throw new ResourceNotFoundException("User with requested id not found");
        }
        return userMapper.toUserResponse(user.get());
    }

    @Override
    public void changePassword(Authentication authentication, PasswordChangeRequest request) {
        User user = ((User) authentication.getPrincipal());

        if(!passwordEncoder.matches(request.currentPassword(),user.getPassword())){
            throw new ResourceNotFoundException("Current password does not match");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepo.save(user);

    }

    @Override
    public void deleteById(Long id) {
        User user=userRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found"));
        Page<Entry> entries=entryRepo.findByUser_UserId(id,null);
        Optional<UserSettings> settings=userSettingsRepo.findByUser_UserId(id);

        settings.ifPresent(userSettingsRepo::delete);
        entryRepo.deleteAll(entries);
        userRepo.delete(user);
    }

    @Override
    public UserResponse findAuthenticatedUserData(Authentication authentication){
        User user=(User) authentication.getPrincipal();
        return userMapper.toUserResponse(user);
    }
}
