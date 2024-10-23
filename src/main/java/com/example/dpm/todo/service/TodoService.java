package com.example.dpm.todo.service;

import org.springframework.stereotype.Service;

import com.example.dpm.member.model.MemberEntity;
import com.example.dpm.todo.dto.TodoDto;
import com.example.dpm.todo.model.TodoEntity;

@Service
public interface TodoService {
	public TodoDto get(int todoId);
	public int AddTodo(TodoDto dto);
	public void modify(TodoDto dto);
	public void remove(int todoId);
	
	 // Entity to DTO
    default TodoDto toDto(TodoEntity todoEntity) {
        return TodoDto.builder()
                .todoId(todoEntity.getTodoId())
                .memberId(todoEntity.getMember().getMember_id())
                .title(todoEntity.getTitle())
                .content(todoEntity.getContent())	
                .dueDate(todoEntity.getDueDate())
                .status(todoEntity.isStatus())
                .build();
    }

    // DTO to Entity
    default TodoEntity toEntity(TodoDto todoDTO, MemberEntity member) {
        return TodoEntity.builder()
                .todoId(todoDTO.getTodoId())
                .member(member)
                .title(todoDTO.getTitle())
                .content(todoDTO.getContent())
                .dueDate(todoDTO.getDueDate())
                .status(todoDTO.isStatus())
                .build();
    }
}
