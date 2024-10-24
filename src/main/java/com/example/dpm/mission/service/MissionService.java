package com.example.dpm.mission.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.dpm.member.model.MemberEntity;
import com.example.dpm.mission.dto.MissionDto;
import com.example.dpm.mission.model.MissionEntity;;

@Service
public interface MissionService {
	public MissionDto getCompleteMission(int missionId);
	public String AddMissionImg(MultipartFile image, MissionDto missionDto) throws IOException;
	public byte[] downLoadImageFileSystem(int missionId) throws IOException;
	public List<MissionEntity> findAll();
    
 // Entity to DTO
    default MissionDto toDto(MissionEntity missionEntity) {
        return MissionDto.builder()
                .missionId(missionEntity.getMissionId())
                .memberId(missionEntity.getMember().getMember_id())
                .img(missionEntity.getImg())
                .missionDate(missionEntity.getMissionDate())
                .build();
    }

    // DTO to Entity
    default MissionEntity toEntity(MissionDto missionDTO, MemberEntity member) {
        return MissionEntity.builder()
                .missionId(missionDTO.getMissionId())
                .member(member)
                .img(missionDTO.getImg())
                .missionDate(missionDTO.getMissionDate())
                .build();
    }
    
}
