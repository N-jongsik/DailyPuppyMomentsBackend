package com.example.dpm.todo.model;

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
@Table(name = "todo")
public class TodoEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int todoId;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private MemberEntity member; 

	@Column(nullable = false)
	private String title; 

	@Column(nullable = false)
	private String content; 

	@Column(nullable = false)
	private LocalDate dueDate; 

	private boolean status; 

}