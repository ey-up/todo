package com.thy.todo.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

@Getter
public class LoginUserResponse {
    private String jwt;

    @JsonIgnore
    private String refreshToken;

    @JsonIgnore
    private long expiresIn;

    public LoginUserResponse(String jwt, String refreshToken, long expiresIn) {
        this.jwt = jwt;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }
}
