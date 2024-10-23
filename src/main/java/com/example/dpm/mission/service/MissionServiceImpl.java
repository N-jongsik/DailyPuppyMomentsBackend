package com.example.dpm.mission.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.dpm.member.model.MemberEntity;
import com.example.dpm.mission.dto.MissionDto;
import com.example.dpm.mission.model.MissionEntity;
import com.example.dpm.mission.repository.MissionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MissionServiceImpl implements MissionService{
	private final MissionRepository missionRepository;
	
	private final String FOLDER_PATH = "c:\\images\\";

	@Override
	public MissionDto getCompleteMission(int missionId) {
		Optional<MissionEntity> result = missionRepository.findById(missionId);
		MissionEntity mission= result.orElseThrow();
		return toDto(mission);
	}

//	@Override
//	public String AddMissionImg(int missionId, MultipartFile image) throws IOException {
//	    // 파일 저장 경로 생성
//	    String fileName = image.getOriginalFilename();
//	    String filePath = FOLDER_PATH + fileName;
//	    
//	    // 파일 저장
//	    File file = new File(filePath);
//	    image.transferTo(file);
//	    
//	    // 미션 가져오기
//	    Optional<MissionEntity> missionOptional = missionRepository.findById(missionId);
//	    MissionEntity mission = missionOptional.orElseThrow(() -> new RuntimeException("Mission not found"));
//
//	    // 미션 엔티티에 파일 정보 저장 (이미지 파일 경로 등을 저장할 수 있음)
//	    //mission.setImagePath(filePath); // 미션 엔티티에 이미지 경로 저장
//	    missionRepository.save(mission);
//	    
//	    return "File uploaded and mission updated successfully!";
//	}


	@Override
	public List<MissionEntity> findAll() {
		return missionRepository.findAll();
	}

}
