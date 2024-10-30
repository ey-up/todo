package com.thy.todo.model;

import com.thy.todo.dto.request.TodoRequest;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Entity
@Data
@Table(name = "Todos")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String task;

    private boolean isCompleted;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static Todo create(TodoRequest request) {
        if (Objects.isNull(request)) return null;
        Todo todo = new Todo();
        todo.setTask(request.getTask());
        todo.setCompleted(request.isCompleted());
        User user = new User();
        user.setId(request.getUserId());
        todo.setUser(user);
        return todo;
    }

    public Todo update(TodoRequest todoRequest) {
        this.setTask(todoRequest.getTask());
        this.setCompleted(todoRequest.isCompleted());
        return this;
    }
}
