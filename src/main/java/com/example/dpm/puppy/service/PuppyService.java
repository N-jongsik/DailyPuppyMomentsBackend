package com.example.dpm.puppy.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.dpm.member.model.MemberEntity;
import com.example.dpm.puppy.dto.PuppyDto;
import com.example.dpm.puppy.model.PuppyEntity;
import com.example.dpm.puppy.model.PuppyImgEntity;

@Service
public interface PuppyService {
	public PuppyDto get(int puppyId);
	public int AddPuppyInfo(PuppyDto dto);
	public void modify(PuppyDto dto);
	public void remove(int puppyId);
	
	 // Entity to DTO
    default PuppyDto toDto(PuppyEntity puppyEntity) {
        return PuppyDto.builder()
                .puppyId(puppyEntity.getPuppyId())
                .memberId(puppyEntity.getMember().getMember_id()) // Extract memberId
                .name(puppyEntity.getName())
                .birth(puppyEntity.getBirth())
                .weightId(puppyEntity.getWeightID())
                .build();
    }

    // DTO to Entity
    default PuppyEntity toEntity(PuppyDto puppyDTO, MemberEntity member, PuppyImgEntity puppyImgEntity) {
       
    	
    	return PuppyEntity.builder()
                .puppyId(puppyDTO.getPuppyId())
                .member(member) // MemberEntity is required here
                .name(puppyDTO.getName())
                .birth(puppyDTO.getBirth())
                .weightID(puppyDTO.getWeightId())
                .img(puppyImgEntity)
                .build();
    }
}
