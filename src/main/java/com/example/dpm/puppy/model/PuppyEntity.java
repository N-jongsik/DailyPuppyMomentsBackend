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
	private MemberEntity member; 

	@Column(nullable = false)
	private String name;

	private LocalDate birth; 

	private String img;

	@Column(nullable = true)
	private int weightID; 
}