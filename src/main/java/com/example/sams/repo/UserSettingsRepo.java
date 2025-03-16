package com.example.sams.repo;

import com.example.sams.entity.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSettingsRepo extends JpaRepository<UserSettings, Long> {
    Optional<UserSettings> findByUser_UserId(Long userId);

}
