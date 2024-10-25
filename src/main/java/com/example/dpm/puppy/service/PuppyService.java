package com.example.dpm.puppy.service;

import java.time.LocalDate;
import java.util.Comparator;

import org.springframework.stereotype.Service;

import com.example.dpm.member.model.MemberEntity;
import com.example.dpm.puppy.dto.PuppyDto;
import com.example.dpm.puppy.model.PuppyEntity;
import com.example.dpm.puppy.model.PuppyImgEntity;
import com.example.dpm.puppy.model.PuppyWeightEntity;


@Service
public interface PuppyService {
	public PuppyDto get(int puppyId);

	public int AddPuppyInfo(PuppyDto dto);

	//public void modify(PuppyDto dto);

	public void remove(int puppyId);

	default PuppyDto toDto(PuppyEntity puppyEntity) {
	    return PuppyDto.builder()
	        .puppyId(puppyEntity.getPuppyId())
	        .memberId(puppyEntity.getMember().getMember_id()) // MemberId 추출
	        .weight(getLatestWeight(puppyEntity))            // 최신 몸무게 추출
	        .name(puppyEntity.getName())
	        .birth(puppyEntity.getBirth())
	        .build();
	}

	// 최신 몸무게를 가져오는 메서드 추가
	private Double getLatestWeight(PuppyEntity puppyEntity) {
	    return puppyEntity.getWeights().stream()
	        .sorted(Comparator.comparing(PuppyWeightEntity::getUploadDate).reversed()) // 기록된 시간으로 정렬하여 가장 최근 값 추출
	        .findFirst()
	        .map(PuppyWeightEntity::getWeight)
	        .orElse(null);  // 몸무게 기록이 없을 때 null 처리
	}
	// DTO to Entity
//	default PuppyEntity toEntity(PuppyDto puppyDTO, MemberEntity member, PuppyImgEntity puppyImgEntity) {
//
//		return PuppyEntity.builder().puppyId(puppyDTO.getPuppyId()).member(member) // MemberEntity is required here
//				.name(puppyDTO.getName()).birth(puppyDTO.getBirth()).weightID(puppyDTO.getWeightId())
//				.img(puppyDTO.getImg()).build();
//	}

	default PuppyEntity toEntity(PuppyDto puppyDto, MemberEntity member) {
		return PuppyEntity.builder().puppyId(puppyDto.getPuppyId()).member(member).name(puppyDto.getName())
				.birth(puppyDto.getBirth()).img(puppyDto.getImg()).weightID(puppyDto.getWeightId()).build();
	}
}