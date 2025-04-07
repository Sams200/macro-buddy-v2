package com.example.sams.service.implementation;

import com.example.sams.entity.Entry;
import com.example.sams.entity.Food;
import com.example.sams.entity.User;
import com.example.sams.enumeration.MealType;
import com.example.sams.enumeration.Role;
import com.example.sams.exception.ResourceNotFoundException;
import com.example.sams.mapper.EntryMapper;
import com.example.sams.repo.EntryRepo;
import com.example.sams.repo.FoodRepo;
import com.example.sams.repo.UserRepo;
import com.example.sams.request.user.EntryRequest;
import com.example.sams.response.EntryResponse;
import com.example.sams.service.IEntryService;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class EntryService implements IEntryService {
    private final UserRepo userRepo;
    private final EntryRepo entryRepo;
    private final EntryMapper entryMapper;
    private final FoodRepo foodRepo;

    @Override
    public Page<EntryResponse> findByUser(Authentication authentication, Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        Page<Entry> entries=entryRepo.findByUser_UserId(user.getUserId(), pageable);
        if(entries.isEmpty()){
            //throw new ResourceNotFoundException("Entries not found");
            return new PageImpl<>(Collections.emptyList());
        }
        
        return entries.map(entryMapper::toEntryResponse);
    }

    @Override
    public Page<EntryResponse> findByUserAndDate(Authentication authentication, LocalDate date, Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        Page<Entry> entries=
                entryRepo.findByUser_UserIdAndDate(user.getUserId(), date,pageable);

        if(entries.isEmpty()){
            return new PageImpl<>(Collections.emptyList());
            //throw new ResourceNotFoundException("Entries not found");
        }

        return entries.map(entryMapper::toEntryResponse);
    }

    @Override
    public void deleteById(Authentication authentication,Long id) {
        User user = (User) authentication.getPrincipal();
        Entry entry = entryRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Entry not found with id"));

        if(!entry.getUser().getUserId().equals(user.getUserId())
                && user.getRole().equals(Role.ROLE_USER)){
            throw new InsufficientAuthenticationException("You do not have permission to delete this entry");
        }
        entryRepo.delete(entry);
    }

    @Override
    public EntryResponse save(Authentication authentication, @Valid EntryRequest request){
        User user = (User) authentication.getPrincipal();
        Food food=foodRepo.findById(request.foodId()).orElseThrow(()->new ResourceNotFoundException("Food not found with id"));

        Entry entry=Entry.builder()
                .quantity(request.quantity())
                .meal(MealType.getInstance(request.meal()))
                .date(request.date())
                .food(food)
                .user(user)
                .build();

        return entryMapper.toEntryResponse(entryRepo.save(entry));
    }

    @Override
    public EntryResponse update(Authentication authentication, @Valid EntryRequest request, Long entryId){
        User user = (User) authentication.getPrincipal();
        Entry entry = entryRepo.findById(entryId).orElseThrow(()->new ResourceNotFoundException("Entry not found with id"));

        if(!entry.getUser().getUserId().equals(user.getUserId())
                && user.getRole().equals(Role.ROLE_USER)){
            throw new InsufficientAuthenticationException("You do not have permission to delete this entry");
        }

        entry.setQuantity(request.quantity());
        entry.setMeal(MealType.getInstance(request.meal()));

        return entryMapper.toEntryResponse(entryRepo.save(entry));
    }


}
