package com.example.dpm.puppy.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dpm.member.model.MemberEntity;
import com.example.dpm.member.repository.MemberRepository;
import com.example.dpm.puppy.dto.PuppyDto;
import com.example.dpm.puppy.model.PuppyEntity;
import com.example.dpm.puppy.model.PuppyImgEntity;
import com.example.dpm.puppy.model.PuppyWeightEntity;
import com.example.dpm.puppy.repository.PuppyImgRepository;
import com.example.dpm.puppy.repository.PuppyRepository;
import com.example.dpm.puppy.repository.PuppyWeightRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PuppyServiceImpl implements PuppyService {
	private final PuppyRepository puppyRepository;
	private final MemberRepository memberRepository;
	private final PuppyImgRepository puppyImgRepository;
	private final PuppyWeightRepository puppyWeightRepository;

	@Override // 한마리 정보 조회
	public PuppyDto get(int puppyId) {
		Optional<PuppyEntity> result = puppyRepository.findById(puppyId);
		PuppyEntity puppy = result.orElseThrow();
		return toDto(puppy);
	}

	@Transactional
	@Override
	public int AddPuppyInfo(PuppyDto dto) {
		Long memberId = dto.getMemberId();
		Optional<MemberEntity> memberOptional = memberRepository.findById(memberId);
		MemberEntity member = memberOptional.orElseThrow();
//				-> new RuntimeException("Member not found")); // 오류 처리 추가

		PuppyImgEntity puppyImgEntity = puppyImgRepository.findById(dto.getImgId())
				.orElseThrow(() -> new RuntimeException("img not found"));

		PuppyEntity puppyEntity = toEntity(dto, member, puppyImgEntity);

		puppyRepository.save(puppyEntity);
		
		// 4. PuppyWeightEntity 생성 및 저장
	    PuppyWeightEntity puppyWeightEntity = PuppyWeightEntity.builder()
	        .weight(dto.getWeight())  // DTO에서 받은 weight
	        .puppy(puppyEntity)  // 새로 등록된 puppy와 연결
	        .build();

	    puppyWeightRepository.save(puppyWeightEntity);  // 몸무게 정보 저장
		
		return puppyEntity.getPuppyId();
	}
//
//	@Override // 몸무게 수정
//	public void modify(PuppyDto dto) {
//	    // 해당 PuppyEntity 찾기
//	    PuppyEntity puppy = puppyRepository.findById(dto.getPuppyId())
//	        .orElseThrow(() -> new IllegalArgumentException("Invalid puppy ID"));
//
//	    // 새롭게 업데이트된 몸무게 정보가 있다고 가정
//	    List<PuppyWeightEntity> updatedWeights = dto.getW.stream()
//	        .map(weightDto -> PuppyWeightEntity.builder()
//	            .weight(weightDto.getWeight())
//	            .uploadDate(weightDto.getUploadDate()) // 새로운 몸무게와 날짜 설정
//	            .puppy(puppy) // 해당 강아지와 연결
//	            .build())
//	        .collect(Collectors.toList());
//
//	    // 기존 몸무게를 업데이트된 몸무게로 교체
//	    puppy.setWeights(updatedWeights);
//
//	    // 저장
//	    puppyRepository.save(puppy);
//	}


	@Override
	public void remove(int puppyId) {
		Optional<PuppyEntity> puppy = puppyRepository.findById(puppyId);
		if (puppy.isPresent()) {
			puppyRepository.deleteById(puppyId);
		} else {
			throw new RuntimeException("해당 반려견이 존재하지 않습니다.");
		}

	}

}