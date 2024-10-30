package com.thy.todo.dto.response;

import com.thy.todo.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;

    public static UserResponse create(User user) {
        if (Objects.isNull(user)) return null;

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setFullName(user.getFullName());
        userResponse.setEmail(user.getEmail());
        return userResponse;
    }
}
