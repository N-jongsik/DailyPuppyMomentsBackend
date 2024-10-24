package com.example.dpm.mission.model;

import java.time.LocalDate;

import com.example.dpm.member.model.MemberEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "mission")
public class MissionEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int missionId;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private MemberEntity member; // Reference to Member entity

	@Column(nullable = false)
	private LocalDate missionDate; // Mission date

	@OneToOne
	@JoinColumn(name = "mission_img_id", nullable = false)
	private MissionImgEntity img; // 강아지 사진
}