package com.example.dpm.puppy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.dpm.puppy.dto.PuppyDto;
import com.example.dpm.puppy.model.PuppyImgEntity;
import com.example.dpm.puppy.repository.PuppyImgRepository;

import java.io.File;
import java.io.IOException;

@Service
public class PuppyImgServiceImpl implements PuppyImgService {

	private static final String FOLDER_PATH = "c:\\images\\"; // 이미지 저장 경로

	@Autowired
	private PuppyImgRepository puppyImgRepository; // ImgEntity를 저장하기 위한 리포지토리

	@Override
	public PuppyImgEntity uploadImage(MultipartFile image) throws IOException {
		// 파일 저장 경로 및 파일 이름 설정
		String filePath = FOLDER_PATH + image.getOriginalFilename();

		// 파일을 지정한 경로에 저장
		File destinationFile = new File(filePath);
		image.transferTo(destinationFile); // 파일 저장

		// ImgEntity 생성 및 저장
		PuppyImgEntity puppyImgEntity = new PuppyImgEntity();
		puppyImgEntity.setFilePath(filePath);

		return puppyImgRepository.save(puppyImgEntity); // 데이터베이스에 저장 후 반환
	}

}
