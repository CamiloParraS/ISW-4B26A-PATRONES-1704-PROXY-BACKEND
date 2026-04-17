package com.slop.gpt.model;

public class QuotaStatusSnapshot {
    private final String userId;
    private final Plan plan;
    private final long monthlyTokensUsed;
    private final Long monthlyTokensRemaining;
    private final String monthlyResetDate;

    public QuotaStatusSnapshot(String userId, Plan plan, long monthlyTokensUsed,
            Long monthlyTokensRemaining, String monthlyResetDate) {
        this.userId = userId;
        this.plan = plan;
        this.monthlyTokensUsed = monthlyTokensUsed;
        this.monthlyTokensRemaining = monthlyTokensRemaining;
        this.monthlyResetDate = monthlyResetDate;
    }

    public String getUserId() {
        return userId;
    }

    public Plan getPlan() {
        return plan;
    }

    public long getMonthlyTokensUsed() {
        return monthlyTokensUsed;
    }

    public Long getMonthlyTokensRemaining() {
        return monthlyTokensRemaining;
    }

    public String getMonthlyResetDate() {
        return monthlyResetDate;
    }
}
