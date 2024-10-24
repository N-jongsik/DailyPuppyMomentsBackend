package com.example.dpm.mission.model;

import java.time.LocalDate;

import com.example.dpm.member.model.MemberEntity;
import com.example.dpm.puppy.model.PuppyEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "mission_image")
public class MissionImgEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer imgId;
	
	private String filePath;
	
	@OneToOne(mappedBy = "img")
	private MissionEntity mission;
}
