package com.slop.gpt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserLoginRequestDto {
    @NotBlank(message = "identifier is required")
    @Size(max = 120, message = "identifier must not exceed 120 characters")
    private String identifier;

    @NotBlank(message = "encryptedPassword is required")
    @Size(max = 400, message = "encryptedPassword must not exceed 400 characters")
    private String encryptedPassword;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }
}
