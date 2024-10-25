package com.example.dpm.puppy.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dpm.puppy.dto.PuppyWeightDto;
import com.example.dpm.puppy.model.PuppyEntity;
import com.example.dpm.puppy.model.PuppyWeightEntity;
import com.example.dpm.puppy.repository.PuppyRepository;
import com.example.dpm.puppy.repository.PuppyWeightRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PuppyWeightServiceImpl implements PuppyWeightService {
	private final PuppyWeightRepository puppyWeightRepository;
	private final PuppyRepository puppyRepository;

	// 몸무게 추가
	@Transactional
	@Override
	public void addWeight(int puppyId, PuppyWeightDto puppyWeightDto) {
		PuppyEntity puppy = puppyRepository.findById(puppyId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid puppy ID"));

		PuppyWeightEntity puppyWeight = PuppyWeightEntity.builder().puppy(puppy).weight(puppyWeightDto.getWeight())
				.uploadDate(LocalDate.now()).build();

		puppyWeightRepository.save(puppyWeight);
	}

	// 특정 Puppy의 몸무게 전체 기록 조회
	@Override
	@Transactional(readOnly = true)
	public List<PuppyWeightDto> getAllWeights(int puppyId) {
		return puppyWeightRepository.findAll().stream().filter(weight -> weight.getPuppy().getPuppyId() == puppyId)
				.map(this::toDto) // Entity -> DTO 변환
				.collect(Collectors.toList());
	}

	// Entity -> DTO 변환
	public PuppyWeightDto toDto(PuppyWeightEntity weightEntity) {
		return PuppyWeightDto.builder().weight(weightEntity.getWeight()).build();
	}

	// DTO -> Entity 변환
	public PuppyWeightEntity toEntity(PuppyWeightDto weightDto, PuppyEntity puppyEntity) {
		return PuppyWeightEntity.builder().weight(weightDto.getWeight()).puppy(puppyEntity).build();
	}
}
