package com.example.sams.service;

import com.example.sams.entity.User;
import com.example.sams.request.user.EntryRequest;
import com.example.sams.response.EntryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface IEntryService {
    List<EntryResponse> findByUser(Long userId);
    void deleteById(Long id);
    EntryResponse create(Long userId, EntryRequest request);
    List<EntryResponse> findByUserIdAndDate(Long userId, LocalDate date);
    void save(Long userId, Long entryId, EntryRequest request);
}
