package com.thy.todo.service;

import com.thy.todo.exception.UserNotFoundException;
import com.thy.todo.model.User;
import com.thy.todo.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id + " not found"));
    }

    public User create(User user) {
        return userRepository.save(user);
    }
}
