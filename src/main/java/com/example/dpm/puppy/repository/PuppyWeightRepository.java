package com.example.dpm.puppy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.dpm.puppy.model.PuppyWeightEntity;

@Repository
public interface PuppyWeightRepository extends JpaRepository<PuppyWeightEntity, Integer>{
	List<PuppyWeightEntity> findByPuppy_PuppyId(int puppyId);  // 특정 강아지의 몸무게 기록을 조회하는 메서드
}
