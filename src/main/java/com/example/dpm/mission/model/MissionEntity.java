package com.example.dpm.mission.model;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import com.example.dpm.member.model.MemberEntity;

import jakarta.persistence.*;
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

    private String img; //사진 경로로 저장

    @Column(nullable = false)
    private LocalDate missionDate; // Mission date
}