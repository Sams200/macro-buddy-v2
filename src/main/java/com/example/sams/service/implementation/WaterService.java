package com.example.sams.service.implementation;

import com.example.sams.entity.User;
import com.example.sams.entity.Water;
import com.example.sams.enumeration.Role;
import com.example.sams.exception.ResourceAlreadyExistsException;
import com.example.sams.exception.ResourceNotFoundException;
import com.example.sams.mapper.WaterMapper;
import com.example.sams.repo.WaterRepo;
import com.example.sams.request.user.WaterRequest;
import com.example.sams.response.WaterResponse;
import com.example.sams.service.IWaterService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;

@Service
@Transactional
@RequiredArgsConstructor
public class WaterService implements IWaterService {
    private final WaterRepo waterRepo;
    private final WaterMapper waterMapper;

    @Override
    public Page<WaterResponse> findByUser(Authentication authentication, Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        Page<Water> waters=waterRepo.findByUser_UserId(user.getUserId(), pageable);
        if(waters.isEmpty()){
            return new PageImpl<>(Collections.emptyList());
        }

        return waters.map(waterMapper::toResponse);
    }

    @Override
    public WaterResponse findByUserAndDate(Authentication authentication, LocalDate localDate) {
        User user = (User) authentication.getPrincipal();
        Water water=waterRepo.findByUser_UserIdAndDate(user.getUserId(), localDate);
        if(water==null){
            // Return a dummy water value
            water=Water.builder()
                    .user(user)
                    .date(localDate)
                    .amount(0)
                    .build();
        }
        return waterMapper.toResponse(water);
    }

    @Override
    public void deleteById(Authentication authentication, Long id) {
        User user = (User) authentication.getPrincipal();
        Water water = waterRepo.findById(id).orElseThrow(()->new ResourceAlreadyExistsException("Water not found with id"));

        if(!water.getUser().getUserId().equals(user.getUserId()) && user.getRole().equals(Role.ROLE_USER)){
            throw new InsufficientAuthenticationException("You do not have permission to delete this water");
        }
        waterRepo.deleteById(id);
    }



    @Override
    public WaterResponse save(Authentication authentication, @Valid WaterRequest waterRequest) {
        User user = (User) authentication.getPrincipal();
        Water water=waterRepo.findByUser_UserIdAndDate(user.getUserId(),waterRequest.date());

        if(!(water==null)){
            return update(authentication,waterRequest,water.getWaterId());
        }

        water=Water.builder()
                .date(waterRequest.date())
                .amount(waterRequest.quantity())
                .user(user)
                .build();
        return waterMapper.toResponse(waterRepo.save(water));
    }

    @Override
    public WaterResponse update(Authentication authentication, @Valid WaterRequest waterRequest,Long waterId) {
        User user = (User) authentication.getPrincipal();
        Water water= waterRepo.findById(waterId).orElseThrow(()->new ResourceNotFoundException("Water not found with id"));

        if(!water.getUser().getUserId().equals(user.getUserId()) && user.getRole().equals(Role.ROLE_USER)){
            throw new InsufficientAuthenticationException("You do not have permission to delete this water");
        }

        water.setAmount(waterRequest.quantity());
        water.setDate(waterRequest.date());

        return waterMapper.toResponse(waterRepo.save(water));
    }
}
