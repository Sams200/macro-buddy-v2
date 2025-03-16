package com.example.sams.service.implementation;

import com.example.sams.entity.Entry;
import com.example.sams.entity.Food;
import com.example.sams.entity.User;
import com.example.sams.enumeration.MealType;
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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public List<EntryResponse> findByUser(Long userId) {
        List<Entry> entries=entryRepo.findByUser_UserId(userId);
        return entries.stream().map(entryMapper::toEntryResponse).toList();
    }

    @Override
    public List<EntryResponse> findByUserIdAndDate(Long userId, LocalDate date) {
        List<Entry> entries=
                entryRepo.findByUser_UserIdAndDate(userId,date);
        return entries.stream()
                .map(entryMapper::toEntryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        Entry entry = entryRepo.findById(id).orElseThrow(()->new RuntimeException("Entry not found with id"));
        entryRepo.delete(entry);
    }

    @Override
    public EntryResponse create(Long userId, @Valid EntryRequest request) {
        Food food=foodRepo.findById(request.foodId()).orElseThrow(()->new RuntimeException("Food not found with id"));
        User user=userRepo.findById(userId).orElseThrow(()->new RuntimeException("User not found with id"));

        Entry entry=Entry.builder()
                .quantity(request.quantity())
                .meal(MealType.getInstance(request.meal()))
                .date(request.date())
                .food(food)
                .user(user)
                .build();
        Entry newEntry=entryRepo.save(entry);
        return entryMapper.toEntryResponse(newEntry);
    }

    @Override
    public void save(Long userId, Long entryId, @Valid EntryRequest request){
        Food food=foodRepo.findById(request.foodId()).orElseThrow(()->new RuntimeException("Food not found with id"));
        User user=userRepo.findById(userId).orElseThrow(()->new RuntimeException("User not found with id"));

        Entry entry=Entry.builder()
                .entryId(entryId)
                .quantity(request.quantity())
                .meal(MealType.getInstance(request.meal()))
                .date(request.date())
                .food(food)
                .user(user)
                .build();

        entryRepo.save(entry);
    }


}
