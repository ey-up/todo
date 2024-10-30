package com.thy.todo.service;

import com.thy.todo.dto.request.TodoRequest;
import com.thy.todo.dto.response.AllTodoResponse;
import com.thy.todo.dto.response.TodoResponse;
import com.thy.todo.exception.TodoDeletionException;
import com.thy.todo.exception.TodoNotFoundException;
import com.thy.todo.model.Todo;
import com.thy.todo.repository.TodoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public TodoResponse addTodo(TodoRequest addTodoRequest) {
        Todo todo = Todo.create(addTodoRequest);
        if (!Objects.isNull(todo)) {
            Todo save = todoRepository.save(todo);
            return TodoResponse.create(save);
        } else {
            throw new TodoNotFoundException("Todo could not be added");
        }

    }

    public TodoResponse getTodo(Long userId, Long todoId) {
        Optional<Todo> todo = todoRepository.findByIdAndUserId(todoId, userId);
        return todo.map(TodoResponse::create)
                .orElseThrow(() -> new TodoNotFoundException("Todo with id " + todoId + " not found for user " + userId));
    }

    public void deleteTodo(Long userId, Long todoId) {
        try {
            todoRepository.deleteByIdAndUserId(todoId, userId);
        } catch (RuntimeException e) {
            throw new TodoDeletionException("Todo could not be deleted");
        }
    }

    public List<TodoResponse> getAllTodo(Long userId) {
        List<Todo> todoList = todoRepository.findAllByUserId(userId);
        return todoList.stream().map(TodoResponse::create).toList();
    }

    @Transactional
    public TodoResponse update(TodoRequest todoRequest, Long todoId) {
        return todoRepository.findByIdAndUserId(todoId, todoRequest.getUserId())
                .map(todo -> todo.update(todoRequest))
                .map(todoRepository::save)
                .map(TodoResponse::create)
                .orElseThrow(() -> new TodoNotFoundException(todoId + " Not found"));
    }

    public AllTodoResponse getTodoByPage(Long userId, int page, int size) {
        Pageable pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Todo> todoList = todoRepository.findByUserId(userId, pageRequest);

        List<TodoResponse> todoResponses = todoList.toList().parallelStream().map(TodoResponse::create).toList();
        AllTodoResponse allTodoResponse = new AllTodoResponse();
        allTodoResponse.setTodos(todoResponses);
        allTodoResponse.setTotalCount(todoList.getTotalElements());

        // todo add error handling
        return allTodoResponse;
    }

    public AllTodoResponse getTodoByPageAndSearch(Long userId, int page, int size, String searched) {
        Pageable pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Todo> todoList  = todoRepository.findByUserId(userId, searched, pageRequest);

        List<TodoResponse> todoResponses = todoList.toList().stream().map(TodoResponse::create).toList();
        AllTodoResponse allTodoResponse = new AllTodoResponse();
        allTodoResponse.setTodos(todoResponses);
        allTodoResponse.setTotalCount(todoList.getTotalElements());

        return allTodoResponse;
    }
}
