package com.example.dpm.mission.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.example.dpm.mission.model.MissionImgEntity;

public interface MissionImgService {

	MissionImgEntity uploadImage(MultipartFile image) throws IOException;

}