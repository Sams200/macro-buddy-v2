package com.example.sams.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Macro {

    private Integer kcal;
    private Float protein;
    private Float fat;
    private Float carbs;

    public Macro(Integer kcal, Float protein, Float fat, Float carbs) {
        this.protein = protein;
        this.kcal = kcal;
        this.fat = fat;
        this.carbs = carbs;
    }

    public void addMacro(Macro macro, float multiplier) {
        this.kcal += (int) (macro.kcal * multiplier);
        this.protein += macro.protein * multiplier;
        this.fat += macro.fat * multiplier;
        this.carbs += macro.carbs * multiplier;
    }
}