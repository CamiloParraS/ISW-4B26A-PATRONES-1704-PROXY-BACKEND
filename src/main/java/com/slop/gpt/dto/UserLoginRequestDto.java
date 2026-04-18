package com.slop.gpt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserLoginRequestDto {
    @NotBlank(message = "identifier is required")
    @Size(max = 120, message = "identifier must not exceed 120 characters")
    private String identifier;

    @NotBlank(message = "password is required")
    @Size(max = 400, message = "password must not exceed 400 characters")
    private String password;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
