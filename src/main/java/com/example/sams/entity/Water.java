package com.example.sams.entity;

import com.example.sams.enumeration.MealType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="water_intake")
public class Water {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "water_intake_id")
    private Long waterId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "amount_ml")
    private Integer amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
}
