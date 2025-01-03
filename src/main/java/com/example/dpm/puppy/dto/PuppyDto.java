package com.example.dpm.puppy.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PuppyDto {
	private int puppyId;
	private Long memberId; // Decoupled member reference by storing only memberId
	private String name;
	private LocalDate birth;
	private int weightId;
	private String img;
}