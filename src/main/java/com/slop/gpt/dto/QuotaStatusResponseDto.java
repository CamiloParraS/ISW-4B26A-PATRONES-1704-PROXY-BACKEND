package com.slop.gpt.dto;

import com.slop.gpt.model.Plan;

public class QuotaStatusResponseDto {
    private String userId;
    private Plan currentPlan;
    private long monthlyTokensUsed;
    private Long monthlyTokensRemaining;
    private String monthlyResetDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Plan getCurrentPlan() {
        return currentPlan;
    }

    public void setCurrentPlan(Plan currentPlan) {
        this.currentPlan = currentPlan;
    }

    public long getMonthlyTokensUsed() {
        return monthlyTokensUsed;
    }

    public void setMonthlyTokensUsed(long monthlyTokensUsed) {
        this.monthlyTokensUsed = monthlyTokensUsed;
    }

    public Long getMonthlyTokensRemaining() {
        return monthlyTokensRemaining;
    }

    public void setMonthlyTokensRemaining(Long monthlyTokensRemaining) {
        this.monthlyTokensRemaining = monthlyTokensRemaining;
    }

    public String getMonthlyResetDate() {
        return monthlyResetDate;
    }

    public void setMonthlyResetDate(String monthlyResetDate) {
        this.monthlyResetDate = monthlyResetDate;
    }
}
