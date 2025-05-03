package com.example.sams.service;

import com.example.sams.request.user.WaterRequest;
import com.example.sams.response.WaterResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public interface IWaterService {
    Page<WaterResponse> findByUser(Authentication authentication, Pageable pageable);
    void deleteById(Authentication authentication, Long id);
    WaterResponse findByUserAndDate(Authentication authentication, LocalDate localDate);
    WaterResponse save(Authentication authentication, WaterRequest waterRequest);
    WaterResponse update(Authentication authentication, WaterRequest waterRequest, Long waterId);
}
