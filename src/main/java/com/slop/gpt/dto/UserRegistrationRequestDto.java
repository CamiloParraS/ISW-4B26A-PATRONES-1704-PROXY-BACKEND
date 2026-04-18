package com.slop.gpt.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegistrationRequestDto {
    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    @Size(max = 120, message = "email must not exceed 120 characters")
    private String email;

    @NotBlank(message = "username is required")
    @Size(max = 80, message = "username must not exceed 80 characters")
    private String username;

    @NotBlank(message = "password is required")
    @Size(max = 400, message = "password must not exceed 400 characters")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
