package com.example.dpm.mission.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.dpm.member.service.MemberService;
import com.example.dpm.mission.dto.MissionDto;
import com.example.dpm.mission.model.MissionEntity;
import com.example.dpm.mission.model.MissionImgEntity;
import com.example.dpm.mission.service.MissionImgService;
import com.example.dpm.mission.service.MissionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MissionController {
	final MissionService missionService;

	final MissionImgService missionImgService;
	
	final MemberService memberService;

	private static final String FOLDER_PATH = "c:\\images\\";

	// 이미지 업로드
	@PostMapping("/mission/img")
	public ResponseEntity<Integer> uploadImage(@RequestPart(value = "image") MultipartFile image) {
		try {
			System.out.println("MissionController: " + image);
			MissionImgEntity imgEntity = missionImgService.uploadImage(image);
			return ResponseEntity.status(HttpStatus.CREATED).body(imgEntity.getImgId());
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(0);
		}
	}

	// 미션 완료 버튼 클릭?!ㅋㅋ
	@PostMapping("/mission")
	public ResponseEntity<Map<String, Integer>> register(@RequestBody MissionDto missionDto) {
		try {
			int missionId = missionService.AddMissionImg(missionDto);
			
			// 로그인한 사용자의 포인트 10점 추가
            memberService.updatePoint(missionDto.getMemberId());

			Map<String, Integer> response = Map.of("Number", missionId);
			return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 Created와 함께 반환
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
		}
	}

	@GetMapping("/mission/calendar/list")
	public ResponseEntity<List<MissionEntity>> getMissionList() {
		List<MissionEntity> missionList = missionService.findAll();
		if (!missionList.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(missionList);
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}

	@GetMapping("/mission/{mission_id}")
	public ResponseEntity<MissionDto> get(@PathVariable(name = "mission_id") int missionId) {
		try {
			MissionDto missionDto = missionService.getCompleteMission(missionId);
			return ResponseEntity.status(HttpStatus.OK).body(missionDto);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

//	@PostMapping("/mission/upload")
//	public ResponseEntity<?> upload(@RequestPart MultipartFile image, @RequestPart  MissionDto missionDto
//			) {
//	    try {
//	        String responseMessage = missionService.AddMissionImg(image, missionDto);
//	        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
//	    } catch (Exception e) {
//	        e.printStackTrace();  // 로그에 예외 스택 트레이스를 출력
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//	                             .body("Error: " + e.getMessage()); // 클라이언트에게 오류 메시지 전송
//	    }
//	}

}