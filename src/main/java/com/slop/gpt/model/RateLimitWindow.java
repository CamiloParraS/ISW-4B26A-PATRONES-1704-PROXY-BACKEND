package com.slop.gpt.model;

public class RateLimitWindow {
    private long epochMinute;
    private int requestCount;

    public RateLimitWindow() {}

    public RateLimitWindow(long epochMinute, int requestCount) {
        this.epochMinute = epochMinute;
        this.requestCount = requestCount;
    }

    public long getEpochMinute() {
        return epochMinute;
    }

    public void setEpochMinute(long epochMinute) {
        this.epochMinute = epochMinute;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }
}
