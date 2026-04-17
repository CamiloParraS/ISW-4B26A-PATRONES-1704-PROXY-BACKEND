package com.slop.gpt.model;

public class DailyUsageEntry {
    private final String date;
    private final long tokensUsed;

    public DailyUsageEntry(String date, long tokensUsed) {
        this.date = date;
        this.tokensUsed = tokensUsed;
    }

    public String getDate() {
        return date;
    }

    public long getTokensUsed() {
        return tokensUsed;
    }
}
