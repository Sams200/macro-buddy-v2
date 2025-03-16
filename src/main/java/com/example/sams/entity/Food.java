package com.example.sams.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="foods")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    private Long foodId;

    @Column(name = "name")
    private String name;

    @Column(name = "producer")
    private String producer;

    @Column(name = "serving_size")
    private Float servingSize;

    @Column(name = "serving_units")
    private String servingUnits;

    @Column(name = "kcal")
    private Integer kcal;

    @Column(name = "protein")
    private Float protein;

    @Column(name = "fat")
    private Float fat;

    @Column(name = "carbs")
    private Float carbs;

    public Macro getMacro() {
        return new Macro(kcal,protein,fat,carbs);
    }
}
