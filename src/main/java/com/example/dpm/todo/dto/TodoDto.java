package com.example.dpm.todo.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoDto {
	private int todoId;
	private Long memberId; // Member reference by memberId
	private String title;
	private String content;
	private LocalDate dueDate;
	private boolean status;
}