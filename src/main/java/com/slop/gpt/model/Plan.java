package com.slop.gpt.model;

public enum Plan {
    FREE(10, 50_000), PRO(60, 500_000), ENTERPRISE(-1, -1);

    private final int requestsPerMinute;
    private final long monthlyTokenQuota;

    Plan(int requestsPerMinute, long monthlyTokenQuota) {
        this.requestsPerMinute = requestsPerMinute;
        this.monthlyTokenQuota = monthlyTokenQuota;
    }

    public int getRequestsPerMinute() {
        return requestsPerMinute;
    }

    public long getMonthlyTokenQuota() {
        return monthlyTokenQuota;
    }

    public boolean hasUnlimitedRequests() {
        return requestsPerMinute < 0;
    }

    public boolean hasUnlimitedTokens() {
        return monthlyTokenQuota < 0;
    }
}
