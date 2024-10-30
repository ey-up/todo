package com.thy.todo.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AllTodoResponse {
    private List<TodoResponse> todos;
    private long totalCount;
}
