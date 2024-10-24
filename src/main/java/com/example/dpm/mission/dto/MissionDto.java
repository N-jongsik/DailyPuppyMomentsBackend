package com.example.dpm.mission.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MissionDto {
    private int missionId;
    private Long memberId; // Member reference by memberId
    private String img;
    private LocalDate missionDate;
}
