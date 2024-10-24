package com.example.dpm.puppy.model;

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
@Table(name = "puppy_image")
public class PuppyImgEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer imgId;

	private String filePath;

	@OneToOne(mappedBy = "img")
	private PuppyEntity puppy;

}