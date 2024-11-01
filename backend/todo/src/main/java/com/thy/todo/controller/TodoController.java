package com.thy.todo.controller;

import com.thy.todo.dto.request.TodoRequest;
import com.thy.todo.dto.response.AllTodoResponse;
import com.thy.todo.dto.response.TodoResponse;
import com.thy.todo.security.JwtService;
import com.thy.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;
    private final JwtService jwtService;

    public TodoController(TodoService todoService, JwtService jwtService) {
        this.todoService = todoService;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Get Todo", security = {@SecurityRequirement(name = "bearer-key")})
    @GetMapping("/{id}")
    public ResponseEntity<TodoResponse> get(@PathVariable("id") Long id) {
        Long userId = jwtService.getUserId();
        TodoResponse todoResponse = todoService.getTodo(userId, id);
        return ResponseEntity.ok(todoResponse);
    }

    @Operation(summary = "Add Todo", security = {@SecurityRequirement(name = "bearer-key")})
    @PostMapping
    public ResponseEntity<TodoResponse> post(@RequestBody TodoRequest addTodoRequest) {
        Long userId = jwtService.getUserId();
        addTodoRequest.setUserId(userId);
        TodoResponse todoResponse = todoService.addTodo(addTodoRequest);
        return ResponseEntity.ok(todoResponse);
    }

    @Operation(summary = "Delete Todo", security = {@SecurityRequirement(name = "bearer-key")})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        Long userId = jwtService.getUserId();
        todoService.deleteTodo(userId, id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get All Todos", security = {@SecurityRequirement(name = "bearer-key")})
    @GetMapping
    public ResponseEntity<AllTodoResponse> getAll() {
        Long userId = jwtService.getUserId();
        List<TodoResponse> todoResponses = todoService.getAllTodo(userId);
        AllTodoResponse allTodoResponse = new AllTodoResponse();
        allTodoResponse.setTodos(todoResponses);
        allTodoResponse.setTotalCount(todoResponses.size());
        return ResponseEntity.ok(allTodoResponse);
    }

    @Operation(summary = "Update Todo", security = {@SecurityRequirement(name = "bearer-key")})
    @PutMapping("/{id}")
    public ResponseEntity<TodoResponse> update(@PathVariable Long id, @RequestBody TodoRequest todoRequest) {
        Long userId = jwtService.getUserId();
        todoRequest.setUserId(userId);
        TodoResponse todoResponse = todoService.update(todoRequest, id);
        return ResponseEntity.ok(todoResponse);
    }

    @Operation(summary = "Get All Todos by Page", security = {@SecurityRequirement(name = "bearer-key")})
    @GetMapping("/search")
    public ResponseEntity<AllTodoResponse> getTodoByPage(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "5") int size) {
        Long userId = jwtService.getUserId();
        AllTodoResponse allTodoResponse = todoService.getTodoByPage(userId, page, size);

        return ResponseEntity.ok(allTodoResponse);
    }

    @Operation(summary = "Get All Todos by Page", security = {@SecurityRequirement(name = "bearer-key")})
    @GetMapping("/search/{searched}")
    public ResponseEntity<AllTodoResponse> getTodoBySearch(@PathVariable String searched,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "5") int size) {
        Long userId = jwtService.getUserId();
        AllTodoResponse allTodoResponse = todoService.getTodoByPageAndSearch(userId, page, size, searched);

        return ResponseEntity.ok(allTodoResponse);
    }
}
