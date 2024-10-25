package com.example.dpm.puppy.service;

import java.util.List;

import com.example.dpm.puppy.dto.PuppyWeightDto;

public interface PuppyWeightService {

	//PuppyWeightDto addWeight(PuppyWeightDto puppyWeightDto);

	List<PuppyWeightDto> getAllWeights(int puppyId);

	void addWeight(int puppyId, PuppyWeightDto puppyWeightDto);

}
