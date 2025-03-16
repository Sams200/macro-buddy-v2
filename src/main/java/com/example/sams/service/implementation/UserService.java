package com.example.sams.service.implementation;

import com.example.sams.entity.Entry;
import com.example.sams.entity.User;
import com.example.sams.mapper.UserMapper;
import com.example.sams.repo.EntryRepo;
import com.example.sams.repo.UserRepo;
import com.example.sams.request.user.PasswordChangeRequest;
import com.example.sams.response.UserResponse;
import com.example.sams.service.IUserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


    @Override
    public Page<UserResponse> findAll(Pageable pageable) {
        Page<User> users = userRepo.findAll(pageable);
        if(users.isEmpty()){
            throw new RuntimeException("Users not found");
        }

        return users.map(userMapper::toUserResponse);
    }

    @Override
    public UserResponse findById(Long id) {
        Optional<User> user = userRepo.findById(id);
        if(user.isEmpty()){
            throw new RuntimeException("User with requested id not found");
        }
        return userMapper.toUserResponse(user.get());
    }

    @Override
    public void changePassword(Long userId, @Valid PasswordChangeRequest request) {
        User user=userRepo
                .findById(userId)
                .orElseThrow(()->new RuntimeException("User not found"));

        if(!request.currentPassword().equals(user.getPassword())){
            throw new RuntimeException("Current password does not match");
        }

        if (!request.newPassword().equals(request.confirmNewPassword())) {
            throw new RuntimeException("New passwords do not match");
        }

        user.setPassword(request.newPassword());
        userRepo.save(user);

    }

    @Override
    public void deleteById(Long id) {
        User user=userRepo.findById(id).orElseThrow(()->new RuntimeException("User not found"));
        List<Entry> entries=entryRepo.findByUser_UserId(id);

        entryRepo.deleteAll(entries);
        userRepo.delete(user);
    }
}
