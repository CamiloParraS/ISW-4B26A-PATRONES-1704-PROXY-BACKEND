package com.slop.gpt.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegistrationRequestDto {
    @NotBlank(message = "userId is required")
    @Size(max = 100, message = "userId must not exceed 100 characters")
    private String userId;

    @NotBlank(message = "encryptedPassword is required")
    @Size(max = 400, message = "encryptedPassword must not exceed 400 characters")
    private String encryptedPassword;

    @NotBlank(message = "email is required")
    @Email(message = "email must be valid")
    @Size(max = 120, message = "email must not exceed 120 characters")
    private String email;

    @NotBlank(message = "username is required")
    @Size(max = 80, message = "username must not exceed 80 characters")
    private String username;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

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
}
