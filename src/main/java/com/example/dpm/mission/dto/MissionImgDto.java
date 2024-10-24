package com.example.dpm.mission.dto;

import com.example.dpm.puppy.dto.PuppyImgDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MissionImgDto {
	private Integer imgId;
	
	private String filePath;
}
