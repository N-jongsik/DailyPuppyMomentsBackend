package com.example.dpm.puppyweight.model;

import java.time.LocalDate;

import com.example.dpm.puppy.model.PuppyEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "puppy_weight")
public class PuppyWeightEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @ManyToOne
//    @JoinColumn(name = "weight_id")
    private int weightID; // Unique identifier for the weight

    private double weight;

    private LocalDate uploadDate;
}
