package com.example.sams.service;

import com.example.sams.entity.User;
import com.example.sams.request.user.EntryRequest;
import com.example.sams.response.EntryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface IEntryService {
    Page<EntryResponse> findByUser(Authentication authentication, Pageable pageable);
    void deleteById(Authentication authentication,Long id);
    Page<EntryResponse> findByUserAndDate(Authentication authentication, LocalDate date, Pageable pageable);
    EntryResponse save(Authentication authentication, EntryRequest request);
    EntryResponse update(Authentication authentication, EntryRequest request, Long entryId);
}
