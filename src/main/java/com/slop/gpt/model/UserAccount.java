package com.slop.gpt.model;

import java.util.HashMap;
import java.util.Map;

public class UserAccount {
    private String userId;
    private String encryptedPassword;
    private Plan plan;
    private long monthlyTokensUsed;
    private String currentMonth;
    private String monthlyResetDate;
    private Map<String, Long> dailyUsage;

    public UserAccount() {
        this.dailyUsage = new HashMap<>();
        this.plan = Plan.FREE;
    }

    public UserAccount(String userId, String encryptedPassword, Plan plan, String currentMonth,
            String monthlyResetDate) {
        this.userId = userId;
        this.encryptedPassword = encryptedPassword;
        this.plan = plan;
        this.currentMonth = currentMonth;
        this.monthlyResetDate = monthlyResetDate;
        this.monthlyTokensUsed = 0L;
        this.dailyUsage = new HashMap<>();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public long getMonthlyTokensUsed() {
        return monthlyTokensUsed;
    }

    public void setMonthlyTokensUsed(long monthlyTokensUsed) {
        this.monthlyTokensUsed = monthlyTokensUsed;
    }

    public String getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(String currentMonth) {
        this.currentMonth = currentMonth;
    }

    public String getMonthlyResetDate() {
        return monthlyResetDate;
    }

    public void setMonthlyResetDate(String monthlyResetDate) {
        this.monthlyResetDate = monthlyResetDate;
    }

    public Map<String, Long> getDailyUsage() {
        return dailyUsage;
    }

    public void setDailyUsage(Map<String, Long> dailyUsage) {
        this.dailyUsage = dailyUsage;
    }
}
