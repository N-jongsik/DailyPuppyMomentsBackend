package com.example.dpm.todo.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dpm.todo.dto.TodoDto;
import com.example.dpm.todo.service.TodoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/todo")
public class TodoController {
    final TodoService todoService;
    
    @GetMapping("/{todo_id}") //todo내용 가져오기
    public ResponseEntity<TodoDto> get(@PathVariable (name = "todo_id") int todoId) {
        try {
            TodoDto todoDto = todoService.get(todoId);
            return ResponseEntity.status(HttpStatus.OK).body(todoDto); // 200 OK와 함께 데이터 반환
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }
    
    @PostMapping("/calender") //todo등록
    public ResponseEntity<Map<String, Integer>> register(@RequestBody TodoDto todoDto) {
        System.out.println("TodoController_todoDto: " + todoDto);
        
        try {
            int todoId = todoService.AddTodo(todoDto);
            Map<String, Integer> response = Map.of("Number", todoId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 Created와 함께 반환
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    @PutMapping("/{todo_id}")
    public ResponseEntity<Map<String, String>> modify(@PathVariable (name = "todo_id") int todoId, @RequestBody TodoDto todoDto) {
        try {
            todoDto.setTodoId(todoId);
            todoService.modify(todoDto);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("result", "success")); // 200 OK 반환
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }
    
    @DeleteMapping("/{todo_id}")
    public ResponseEntity<Map<String, String>> remove(@PathVariable(name = "todo_id") int todoId) {
        try {
            todoService.remove(todoId);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("delete", "success")); // 200 OK 반환
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }
}
