package com.slop.gpt.dto;

import java.util.ArrayList;
import java.util.List;

public class QuotaHistoryResponseDto {
    private String userId;
    private List<DailyUsageDto> last7Days;

    public QuotaHistoryResponseDto() {
        this.last7Days = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<DailyUsageDto> getLast7Days() {
        return last7Days;
    }

    public void setLast7Days(List<DailyUsageDto> last7Days) {
        this.last7Days = last7Days;
    }
}
