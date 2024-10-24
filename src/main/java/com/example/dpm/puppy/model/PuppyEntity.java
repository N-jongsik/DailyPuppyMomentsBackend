package com.example.dpm.puppy.model;

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
@Table(name = "puppy")
public class PuppyEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int puppyId;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private MemberEntity member; // Reference to Member entity

	@Column(nullable = false)
	private String name; // Puppy name

	private LocalDate birth; // Birthdate

	@OneToOne
	@JoinColumn(name = "puppy_img_id", nullable = false)
	private PuppyImgEntity img; // 강아지 사진

	// @OneToMany(mappedBy = "puppy") // PuppyWeightEntity와의 관계
	private int weightID; // 여러 개의 weight를 가질 수 있음
}