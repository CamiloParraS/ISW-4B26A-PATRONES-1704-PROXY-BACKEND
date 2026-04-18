package com.slop.gpt.dto;

import com.slop.gpt.model.Plan;

public class UserLoginResponseDto {
    private String userId;
    private String email;
    private String username;
    private Plan currentPlan;
    private String loggedInAt;
    private String token;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Plan getCurrentPlan() {
        return currentPlan;
    }

    public void setCurrentPlan(Plan currentPlan) {
        this.currentPlan = currentPlan;
    }

    public String getLoggedInAt() {
        return loggedInAt;
    }

    public void setLoggedInAt(String loggedInAt) {
        this.loggedInAt = loggedInAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
