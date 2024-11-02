package com.thy.todo.service;

import com.thy.todo.dto.request.TodoRequest;
import com.thy.todo.dto.response.AllTodoResponse;
import com.thy.todo.dto.response.TodoResponse;
import com.thy.todo.exception.DatabaseAccessException;
import com.thy.todo.exception.TodoDeletionException;
import com.thy.todo.exception.TodoNotFoundException;
import com.thy.todo.factory.TestTodoFactory;
import com.thy.todo.model.Todo;
import com.thy.todo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    private TodoRequest todoRequest;
    private Todo todo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        todoRequest = new TodoRequest();
        todo = TestTodoFactory.createSampleTodo();
    }

    @Test
    void addTodo_ShouldReturnTodoResponse() {
        // Given
        todoRequest.setUserId(1L);
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);

        // When
        TodoResponse response = todoService.addTodo(todoRequest);

        // Then
        assertEquals(todo.getId(), response.getId());
        verify(todoRepository, times(1)).save(any(Todo.class));
    }


    @Test
    void addTodo_ShouldThrowDatabaseAccessException() {
        // Given
        todoRequest.setUserId(1L);
        when(todoRepository.save(any(Todo.class))).thenThrow(new DataAccessException("Database error") {});

        // When&Then
        DatabaseAccessException exception = assertThrows(DatabaseAccessException.class, () -> {
            todoService.addTodo(todoRequest);
        });
        assertEquals("Error saving Todo to database", exception.getMessage());
    }

    @Test
    void getTodo_ShouldReturnTodoResponse() {
        // Given
        Long userId = 1L;
        Long todoId = 1L;
        when(todoRepository.findByIdAndUserId(todoId, userId)).thenReturn(Optional.of(todo));

        // When
        TodoResponse response = todoService.getTodo(userId, todoId);

        // Then
        assertEquals(todo.getId(), response.getId());
        verify(todoRepository, times(1)).findByIdAndUserId(todoId, userId);
    }

    @Test
    void getTodo_ShouldThrowTodoNotFoundException_WhenTodoDoesNotExist() {
        // Given
        Long userId = 1L;
        Long todoId = 1L;
        when(todoRepository.findByIdAndUserId(todoId, userId)).thenReturn(Optional.empty());

        // When / Then
        TodoNotFoundException exception = assertThrows(TodoNotFoundException.class, () -> {
            todoService.getTodo(userId, todoId);
        });

        assertEquals("Todo with id " + todoId + " not found for user " + userId, exception.getMessage());
        verify(todoRepository, times(1)).findByIdAndUserId(todoId, userId);
    }

    @Test
    void deleteTodo_ShouldNotThrowException() {
        // Given
        Long userId = 1L;
        Long todoId = 1L;

        // When
        todoService.deleteTodo(userId, todoId);

        // Then
        verify(todoRepository, times(1)).deleteByIdAndUserId(todoId, userId);
    }

    @Test
    void deleteTodo_ShouldThrowTodoDeletionException_WhenDeletionFails() {
        // Given
        Long userId = 1L;
        Long todoId = 1L;
        doThrow(new RuntimeException()).when(todoRepository).deleteByIdAndUserId(todoId, userId);

        // When / Then
        TodoDeletionException exception = assertThrows(TodoDeletionException.class, () -> {
            todoService.deleteTodo(userId, todoId);
        });

        assertEquals("Todo could not be deleted", exception.getMessage());
        verify(todoRepository, times(1)).deleteByIdAndUserId(todoId, userId);
    }

    @Test
    void getAllTodo_ShouldReturnListOfTodoResponses() {
        // Given
        Long userId = 1L;
        when(todoRepository.findAllByUserId(userId)).thenReturn(Collections.singletonList(todo));

        // When
        List<TodoResponse> response = todoService.getAllTodo(userId);

        // Then
        assertEquals(1, response.size());
        assertEquals(todo.getId(), response.get(0).getId());
        verify(todoRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    void update_ShouldReturnUpdatedTodoResponse() {
        // Given
        Long todoId = 1L;
        todoRequest.setUserId(1L);
        when(todoRepository.findByIdAndUserId(todoId, todoRequest.getUserId())).thenReturn(Optional.of(todo));
        when(todoRepository.save(todo)).thenReturn(todo);

        // When
        TodoResponse response = todoService.update(todoRequest, todoId);

        // Then
        assertEquals(todo.getId(), response.getId());
        verify(todoRepository, times(1)).findByIdAndUserId(todoId, todoRequest.getUserId());
        verify(todoRepository, times(1)).save(todo);
    }

    @Test
    void update_ShouldThrowTodoNotFoundException_WhenTodoDoesNotExist() {
        // Given
        Long todoId = 1L;
        todoRequest.setUserId(1L);
        when(todoRepository.findByIdAndUserId(todoId, todoRequest.getUserId())).thenReturn(Optional.empty());

        // When / Then
        TodoNotFoundException exception = assertThrows(TodoNotFoundException.class, () -> {
            todoService.update(todoRequest, todoId);
        });

        assertEquals(todoId + " Not found", exception.getMessage());
        verify(todoRepository, times(1)).findByIdAndUserId(todoId, todoRequest.getUserId());
        verify(todoRepository, never()).save(any());
    }

    @Test
    void testGetTodoByPage_Success() {
        // Given
        Long userId = 1L;
        int page = 0;
        int size = 5;
        Page<Todo> todoPage = new PageImpl<>(List.of(todo));

        when(todoRepository.findByUserId(userId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"))))
                .thenReturn(todoPage);

        // When
        AllTodoResponse response = todoService.getTodoByPage(userId, page, size);

        // Then
        assertNotNull(response);
        assertEquals(1, response.getTodos().size());
    }

    @Test
    void testGetTodoByPage_DatabaseAccessException() {
        // Given
        Long userId = 1L;
        int page = 0;
        int size = 5;

        when(todoRepository.findByUserId(any(Long.class), any(Pageable.class)))
                .thenThrow(new DataAccessException("Database error") {});

        // When
        DatabaseAccessException exception = assertThrows(DatabaseAccessException.class, () -> {
            todoService.getTodoByPage(userId, page, size);
        });

        // Then
        assertEquals("Error fetching Todos from database", exception.getMessage());
    }

    @Test
    void testGetTodoByPageAndSearch_Success() {
        // Given
        Long userId = 1L;
        String searched = "example search";
        int page = 0;
        int size = 5;
        Pageable pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Todo> todoPage = new PageImpl<>(List.of(todo));

        when(todoRepository.findByUserId(userId, searched, pageRequest)).thenReturn(todoPage);

        // When&Then
        AllTodoResponse response = todoService.getTodoByPageAndSearch(userId, page, size, searched);
        assertEquals(1, response.getTodos().size());
        verify(todoRepository, times(1)).findByUserId(userId, searched, pageRequest);
    }

    @Test
    void testGetTodoByPageAndSearch_DatabaseAccessException() {
        // Given
        Long userId = 1L;
        String searched = "example search";
        int page = 0;
        int size = 5;
        Pageable pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        when(todoRepository.findByUserId(userId, searched, pageRequest)).thenThrow(new RuntimeException("Database error"));

        // When&Then
        assertThrows(DatabaseAccessException.class, () -> todoService.getTodoByPageAndSearch(userId, page, size, searched));
        verify(todoRepository, times(1)).findByUserId(userId, searched, pageRequest);
    }
}
