package com.example.dpm.todo.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.dpm.member.model.MemberEntity;
import com.example.dpm.member.repository.MemberRepository;
import com.example.dpm.todo.dto.TodoDto;
import com.example.dpm.todo.model.TodoEntity;
import com.example.dpm.todo.repository.TodoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService{
	private final TodoRepository todoRepository;
	private final MemberRepository memberRepository;

	@Override
	public TodoDto get(int todoId) {
		Optional<TodoEntity> result = todoRepository.findById(todoId);
		TodoEntity todo = result.orElseThrow();
		return toDto(todo);
	}

	@Override
    public int AddTodo(TodoDto dto) {
        Long memberId = dto.getMemberId();
        Optional<MemberEntity> memberOptional = memberRepository.findById(memberId);
        MemberEntity member = memberOptional.orElseThrow(() -> new RuntimeException("Member not found")); // 오류 처리 추가
        TodoEntity todoEntity = toEntity(dto, member);
        todoRepository.save(todoEntity);
        return todoEntity.getTodoId(); // 저장된 todoId 반환
    }

	@Override
	public void modify(TodoDto dto) {
		Optional<TodoEntity> result = todoRepository.findById(dto.getTodoId());
		TodoEntity todo = result.orElseThrow();
		
		todo.setTitle(dto.getTitle());
		todo.setContent(dto.getContent());
		todoRepository.save(todo);
	}

	@Override
	public void remove(int todoId) {
		todoRepository.deleteById(todoId);
	}

}
