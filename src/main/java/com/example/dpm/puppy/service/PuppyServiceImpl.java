package com.example.dpm.puppy.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.dpm.member.model.MemberEntity;
import com.example.dpm.member.repository.MemberRepository;
import com.example.dpm.puppy.dto.PuppyDto;
import com.example.dpm.puppy.model.PuppyEntity;
import com.example.dpm.puppy.repository.PuppyRepository;
import com.example.dpm.todo.model.TodoEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PuppyServiceImpl implements PuppyService{
	private final PuppyRepository puppyRepository;
	private final MemberRepository memberRepository;
	
	@Override
	public PuppyDto get(int puppyId) {
		Optional<PuppyEntity> result = puppyRepository.findById(puppyId);
		PuppyEntity puppy = result.orElseThrow();
		return toDto(puppy);
	}

	@Override
	public int AddPuppyInfo(PuppyDto dto) {
		Long memberId = dto.getMemberId();
		Optional<MemberEntity> memberOptional = memberRepository.findById(memberId);
		MemberEntity member = memberOptional.orElseThrow(() -> new RuntimeException("Member not found")); // 오류 처리 추가
		PuppyEntity puppyEntity = toEntity(dto, member);
		puppyRepository.save(puppyEntity);
		return puppyEntity.getPuppyId();
	}

	@Override
	public void modify(PuppyDto dto) {
		Optional<PuppyEntity> result = puppyRepository.findById(dto.getPuppyId());
		PuppyEntity puppy = result.orElseThrow();
		
		puppy.setBirth(dto.getBirth());
		puppy.setWeight(dto.getWeight());
		puppyRepository.save(puppy);
	}

	@Override
	public void remove(int puppyId) {
		puppyRepository.deleteById(puppyId);
		
	}

}
