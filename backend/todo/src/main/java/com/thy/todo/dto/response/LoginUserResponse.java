package com.thy.todo.dto.response;

import lombok.Getter;

@Getter
public class LoginUserResponse {
    private String jwt;
    private String refreshToken;
    private long expiresIn;

    public LoginUserResponse(String jwt, String refreshToken, long expiresIn) {
        this.jwt = jwt;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }
}
