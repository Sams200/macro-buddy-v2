package com.example.sams.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="user_settings")
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_settings_id")
    private Long userSettingsId;

    @Column(name = "goal_kcal")
    private Integer goalKcal=2020;

    @Column(name = "goal_protein")
    private Float goalProtein=170.0f;

    @Column(name = "goal_fat")
    private Float goalFat=60.0f;

    @Column(name = "goal_carbs")
    private Float goalCarbs=200.0f;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}
