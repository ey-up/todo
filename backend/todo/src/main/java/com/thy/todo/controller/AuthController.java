package com.thy.todo.controller;

import com.thy.todo.dto.request.LoginUserRequest;
import com.thy.todo.dto.request.RegisterUserRequest;
import com.thy.todo.dto.response.LoginUserResponse;
import com.thy.todo.dto.response.UserResponse;
import com.thy.todo.model.User;
import com.thy.todo.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> login(@Valid @RequestBody LoginUserRequest LoginUserRequest) {
        LoginUserResponse loginUserResponse = authenticationService.login(LoginUserRequest);
        return ResponseEntity.ok(loginUserResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        User savedUser = authenticationService.signup(registerUserRequest);
        return ResponseEntity.ok(UserResponse.create(savedUser));
    }

//    @PostMapping("/refresh-token")
//    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
//        String refreshToken = request.getRefreshToken();
//
//        if (isValidRefreshToken(refreshToken)) {
//            String newAccessToken = generateNewAccessToken(refreshToken);
//            return ResponseEntity.ok(new TokenResponse(newAccessToken));
//        }
//        return ResponseEntity.status(401).body("Invalid refresh token");
//    }
}