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
import com.example.dpm.mission.model.MissionImgEntity;
import com.example.dpm.mission.repository.MissionImgRepository;
import com.example.dpm.mission.repository.MissionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MissionServiceImpl implements MissionService{
	private final MissionRepository missionRepository;
	private final MemberRepository memberRepository;
	private final MissionImgRepository missionImgRepository;
	
	@Override
	public MissionDto getCompleteMission(int missionId) {
		Optional<MissionEntity> result = missionRepository.findById(missionId);
		MissionEntity mission= result.orElseThrow();
		return toDto(mission);
	}

	@Override
	public int AddMissionImg(MissionDto missionDto) {
		Optional<MemberEntity> memberOpt = memberRepository.findById(missionDto.getMemberId());//멤버 id가져오기
		MemberEntity member = memberOpt.orElseThrow(() -> new RuntimeException("Member not found"));
		
		MissionImgEntity missionImgEntity = missionImgRepository.findById(missionDto.getImgId())
				.orElseThrow(()-> new RuntimeException("img not found"));
	
		MissionEntity missionEntity = toEntity(missionDto, member, missionImgEntity);
		
		missionRepository.save(missionEntity);
		return missionEntity.getMissionId();
	}
	
	//@Override
	//public byte[] downLoadImageFileSystem(int missionId) throws IOException{
	//	MissionEntity mission = missionRepository.findById(missionId).orElseThrow();
//
//		String filePath = mission.getImg();
//
//		System.out.println("download fileData : " + filePath);
//
//		return Files.readAllBytes(new File(filePath).toPath());
//	}


	@Override
	public List<MissionEntity> findAll() {
		return missionRepository.findAll();
	}

}
