package com.example.dpm.mission.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.dpm.member.model.MemberEntity;
import com.example.dpm.member.repository.MemberRepository;
import com.example.dpm.mission.dto.MissionDto;
import com.example.dpm.mission.model.MissionEntity;
import com.example.dpm.mission.repository.MissionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MissionServiceImpl implements MissionService{
	private final MissionRepository missionRepository;
	private final MemberRepository memberRepository;
	
	private final String FOLDER_PATH = "c:\\images\\";

	@Override
	public MissionDto getCompleteMission(int missionId) {
		Optional<MissionEntity> result = missionRepository.findById(missionId);
		MissionEntity mission= result.orElseThrow();
		return toDto(mission);
	}

	@Override
	public String AddMissionImg(MultipartFile image,MissionDto missionDto) throws IOException {
		Optional<MemberEntity> memberOpt = memberRepository.findById(missionDto.getMemberId());//멤버 id가져오기
		MemberEntity member = memberOpt.orElseThrow(() -> new RuntimeException("Member not found"));
		
		// 파일 저장 경로 생성
	    String fileName = image.getOriginalFilename();
	    String filePath = FOLDER_PATH + fileName;
	    
		//MissionEntity missionEntity = toEntity(missionDto, member);
	    MissionEntity missionEntity = missionRepository.save(MissionEntity.builder()
	    		.missionId(missionDto.getMissionId())
	    		.member(member)
	    		.img(filePath)
	    		.missionDate(LocalDate.now())
	    		.build());
	    
	    //MissionEntity missionImg = missionRepository.save(missionEntity); // 저장

	    image.transferTo(new File(filePath));
	    if(image != null) {
	    	return "File uploaded and mission updated successfully!";
	    }
	    
	    return null;
	}
	
	@Override
	public byte[] downLoadImageFileSystem(int missionId) throws IOException{
		MissionEntity mission = missionRepository.findById(missionId).orElseThrow();

		String filePath = mission.getImg();

		System.out.println("download fileData : " + filePath);

		return Files.readAllBytes(new File(filePath).toPath());
	}


	@Override
	public List<MissionEntity> findAll() {
		return missionRepository.findAll();
	}

}
