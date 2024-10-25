package com.example.dpm.mission.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MissionDto {
	private int missionId;
	private Long memberId; // Member reference by memberId
	private int imgId;
	private LocalDate missionDate;
}