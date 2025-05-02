package com.example.sams.repo;

import com.example.sams.entity.Water;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface WaterRepo extends JpaRepository<Water, Long> {
    Page<Water> findByUser_UserId(Long userUserId, Pageable pageable);
    Page<Water> findByUser_UserIdAndDate(Long userUserId, LocalDate date, Pageable pageable);
}
