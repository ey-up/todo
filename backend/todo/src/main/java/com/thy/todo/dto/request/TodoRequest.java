package com.thy.todo.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoRequest {

    @NotBlank(message = "Task cannot be empty")
    @Size(max = 255, message = "Task cannot exceed 255 characters")
    private String task;

    private boolean isCompleted;

    @JsonIgnore
    private Long userId;
}
