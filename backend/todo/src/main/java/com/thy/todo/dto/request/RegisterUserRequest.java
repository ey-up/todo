package com.thy.todo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserRequest {
    @NotBlank(message = "Email Address cannot be empty")
    @Email(message = "Please enter a valid email address.")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "FullName cannot be empty")
    @Size(min = 2, max = 50, message = "FullName must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Zà-úÀ-Ú\\s]+$", message = "Name must contain only letters and spaces")
    private String fullName;
}
