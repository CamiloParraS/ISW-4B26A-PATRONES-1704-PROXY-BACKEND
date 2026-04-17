package com.slop.gpt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegistrationRequestDto {
    @NotBlank(message = "userId is required")
    @Size(max = 100, message = "userId must not exceed 100 characters")
    private String userId;

    @NotBlank(message = "encryptedPassword is required")
    @Size(max = 400, message = "encryptedPassword must not exceed 400 characters")
    private String encryptedPassword;

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
}
