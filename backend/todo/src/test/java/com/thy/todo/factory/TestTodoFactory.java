package com.thy.todo.factory;

import com.thy.todo.model.Todo;
import com.thy.todo.model.User;

public class TestTodoFactory {

    public static Todo createSampleTodo() {
        Todo todo = new Todo();
        todo.setId(1L);
        User user = new User();
        user.setId(1L);
        todo.setUser(user);
        todo.setTask("Sample Todo");
        return todo;
    }

}
