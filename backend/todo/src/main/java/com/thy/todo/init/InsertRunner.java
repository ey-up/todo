package com.thy.todo.init;

import com.thy.todo.dto.request.TodoRequest;
import com.thy.todo.dto.request.RegisterUserRequest;
import com.thy.todo.model.Todo;
import com.thy.todo.repository.TodoRepository;
import com.thy.todo.service.AuthenticationService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InsertRunner implements ApplicationRunner {

    private final TodoRepository todoRepository;
    private final AuthenticationService authenticationService;

    public InsertRunner(TodoRepository todoRepository, AuthenticationService authenticationService) {
        this.todoRepository = todoRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public void run(ApplicationArguments args) {
        addUsers();
        addTodos();
    }

    private void addTodos() {
        List<TodoRequest> todoRequestList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TodoRequest addTodoRequest = new TodoRequest();
            addTodoRequest.setCompleted(false);
            addTodoRequest.setTask("Evi temizle- "+ (i + 1));
            addTodoRequest.setUserId(1L);
            todoRequestList.add(addTodoRequest);
        }
        List<Todo> list = todoRequestList.stream().map(Todo::create).toList();
        todoRepository.saveAll(list);
    }

    private void addUsers() {
        System.out.println("---------------------------");

        RegisterUserRequest registerUserRequest3 = new RegisterUserRequest();
        registerUserRequest3.setFullName("Test");
        registerUserRequest3.setEmail("test@test.com");
        registerUserRequest3.setPassword("test123");
        authenticationService.signup(registerUserRequest3);

        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setFullName("Testoğlu");
        registerUserRequest.setEmail("testinho@test.com");
        registerUserRequest.setPassword("Testic");
        authenticationService.signup(registerUserRequest);

        RegisterUserRequest registerUserRequest2 = new RegisterUserRequest();
        registerUserRequest2.setFullName("Testico");
        registerUserRequest2.setEmail("testicado@test.com");
        registerUserRequest2.setPassword("Testmann");
        authenticationService.signup(registerUserRequest2);

    }
}
