package com.slop.gpt.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class GenerationRequestDto {
    @NotBlank(message = "userId is required")
    @Size(max = 100, message = "userId must not exceed 100 characters")
    private String userId;

    @NotBlank(message = "prompt is required")
    @Size(max = 8000, message = "prompt must not exceed 8000 characters")
    private String prompt;

    @Min(value = 1, message = "maxOutputTokens must be at least 1")
    @Max(value = 2048, message = "maxOutputTokens must be at most 2048")
    private Integer maxOutputTokens = 120;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public Integer getMaxOutputTokens() {
        return maxOutputTokens;
    }

    public void setMaxOutputTokens(Integer maxOutputTokens) {
        this.maxOutputTokens = maxOutputTokens;
    }
}
