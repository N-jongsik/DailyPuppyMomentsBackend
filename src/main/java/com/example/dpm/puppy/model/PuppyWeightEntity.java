package com.example.dpm.puppy.model;

import java.time.LocalDate;

import com.example.dpm.post.model.PostEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
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
@Table(name = "puppy_weight")
public class PuppyWeightEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int weightId;

	private double weight;

	private LocalDate uploadDate;
	
	@ManyToOne
    @JoinColumn(name = "puppy_id", nullable = false)
    private PuppyEntity puppy;
	
	@PrePersist // 엔티티가 생성될 때 호출
	public void prePersist() {
		this.uploadDate = LocalDate.now(); // 현재 시간으로 초기화
	}
}
