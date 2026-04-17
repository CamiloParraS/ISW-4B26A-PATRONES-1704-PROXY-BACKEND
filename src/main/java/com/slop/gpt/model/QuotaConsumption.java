package com.slop.gpt.model;

public class QuotaConsumption {
    private final boolean allowed;
    private final Plan plan;
    private final long monthlyTokensUsed;
    private final Long monthlyTokensRemaining;
    private final String monthlyResetDate;

    public QuotaConsumption(boolean allowed, Plan plan, long monthlyTokensUsed,
            Long monthlyTokensRemaining, String monthlyResetDate) {
        this.allowed = allowed;
        this.plan = plan;
        this.monthlyTokensUsed = monthlyTokensUsed;
        this.monthlyTokensRemaining = monthlyTokensRemaining;
        this.monthlyResetDate = monthlyResetDate;
    }

    public boolean isAllowed() {
        return allowed;
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
