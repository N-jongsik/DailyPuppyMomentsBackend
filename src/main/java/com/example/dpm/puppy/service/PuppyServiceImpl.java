package com.example.dpm.puppy.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dpm.member.model.MemberEntity;
import com.example.dpm.member.repository.MemberRepository;
import com.example.dpm.puppy.dto.PuppyDto;
import com.example.dpm.puppy.model.PuppyEntity;
import com.example.dpm.puppy.repository.PuppyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PuppyServiceImpl implements PuppyService {
	private final PuppyRepository puppyRepository;
	private final MemberRepository memberRepository;
	// private final PuppyImgRepository puppyImgRepository;

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

//		PuppyImgEntity puppyImgEntity = puppyImgRepository.findById(dto.getImgId())
//				.orElseThrow(() -> new RuntimeException("img not found"));

		// PuppyEntity puppyEntity = toEntity(dto, member, puppyImgEntity);

		PuppyEntity puppyEntity = toEntity(dto, member);

		puppyRepository.save(puppyEntity);
		return puppyEntity.getPuppyId();
	}

	@Override // 몸무게 수정 가능
	public void modify(PuppyDto dto) {
		Optional<PuppyEntity> result = puppyRepository.findById(dto.getPuppyId());
		PuppyEntity puppy = result.orElseThrow();

		puppy.setWeightID(dto.getWeightId());
		puppyRepository.save(puppy);
	}

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