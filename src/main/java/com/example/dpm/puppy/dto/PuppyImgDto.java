package com.example.dpm.puppy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PuppyImgDto {
	private int imgId;
	
	private String filePath;
}