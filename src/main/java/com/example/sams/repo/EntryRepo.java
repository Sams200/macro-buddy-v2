package com.example.sams.repo;

import com.example.sams.entity.Entry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EntryRepo extends JpaRepository<Entry, Long> {

    @Query("SELECT e FROM Entry e WHERE e.user.userId = :userId ORDER BY e.date")
    Page<Entry> findByUser_UserId(@Param("userId") Long userId, Pageable pageable);
    Page<Entry> findByUser_UserIdAndDate(Long userUserId, LocalDate date, Pageable pageable);
    Page<Entry> findByFood_FoodId(Long foodId, Pageable pageable);

}
