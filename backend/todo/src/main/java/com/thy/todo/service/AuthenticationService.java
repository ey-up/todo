package com.thy.todo.service;

import com.thy.todo.dto.request.LoginUserRequest;
import com.thy.todo.dto.request.RegisterUserRequest;
import com.thy.todo.dto.response.LoginUserResponse;
import com.thy.todo.exception.EmailAlreadyTakenException;
import com.thy.todo.exception.UserNotActiveException;
import com.thy.todo.exception.UserNotFoundException;
import com.thy.todo.model.User;
import com.thy.todo.repository.UserRepository;
import com.thy.todo.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public User signup(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyTakenException(request.getEmail() + " email address is already in use");
        }
        User user = User.create(request, passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }

    public LoginUserResponse login(LoginUserRequest loginUserRequest) {
        User user = userRepository.findByEmail(loginUserRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found for email: " + loginUserRequest.getEmail()));

        if (!user.isActive()) {
            throw new UserNotActiveException("User account is inactive for email: " + user.getEmail());
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserRequest.getEmail(), loginUserRequest.getPassword()));

        String jwt = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        long expirationTime = jwtService.getExpirationTime();
        logger.info("User '{}' logged in successfully.", user.getEmail());

        return new LoginUserResponse(jwt, refreshToken, expirationTime);
    }

}
