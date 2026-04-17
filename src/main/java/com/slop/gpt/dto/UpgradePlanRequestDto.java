package com.slop.gpt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpgradePlanRequestDto {
    @NotBlank(message = "userId is required")
    @Size(max = 100, message = "userId must not exceed 100 characters")
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
