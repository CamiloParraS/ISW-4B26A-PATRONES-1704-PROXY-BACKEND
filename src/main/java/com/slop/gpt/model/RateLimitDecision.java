package com.slop.gpt.model;

public class RateLimitDecision {
    private final boolean allowed;
    private final long retryAfterSeconds;
    private final int requestsUsedInCurrentMinute;
    private final Integer requestsRemainingInCurrentMinute;

    public RateLimitDecision(boolean allowed, long retryAfterSeconds,
            int requestsUsedInCurrentMinute, Integer requestsRemainingInCurrentMinute) {
        this.allowed = allowed;
        this.retryAfterSeconds = retryAfterSeconds;
        this.requestsUsedInCurrentMinute = requestsUsedInCurrentMinute;
        this.requestsRemainingInCurrentMinute = requestsRemainingInCurrentMinute;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public long getRetryAfterSeconds() {
        return retryAfterSeconds;
    }

    public int getRequestsUsedInCurrentMinute() {
        return requestsUsedInCurrentMinute;
    }

    public Integer getRequestsRemainingInCurrentMinute() {
        return requestsRemainingInCurrentMinute;
    }
}
