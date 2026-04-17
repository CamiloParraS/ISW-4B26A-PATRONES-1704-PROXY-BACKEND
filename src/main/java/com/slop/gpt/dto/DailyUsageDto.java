package com.slop.gpt.dto;

public class DailyUsageDto {
    private String date;
    private long tokensUsed;

    public DailyUsageDto() {}

    public DailyUsageDto(String date, long tokensUsed) {
        this.date = date;
        this.tokensUsed = tokensUsed;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTokensUsed() {
        return tokensUsed;
    }

    public void setTokensUsed(long tokensUsed) {
        this.tokensUsed = tokensUsed;
    }
}
