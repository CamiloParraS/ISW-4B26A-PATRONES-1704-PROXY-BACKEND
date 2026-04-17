package com.slop.gpt.dto;

import com.slop.gpt.model.Plan;

public class GenerationResponseDto {
    private String userId;
    private String generatedText;
    private long processingTimeMs;

    private Plan currentPlan;
    private int consumedTokens;
    private int promptTokens;
    private int outputTokensEstimate;
    private long monthlyTokensUsed;
    private Long monthlyTokensRemaining;
    private String monthlyResetDate;

    private Integer requestsUsedInCurrentMinute;
    private Integer requestsRemainingInCurrentMinute;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGeneratedText() {
        return generatedText;
    }

    public void setGeneratedText(String generatedText) {
        this.generatedText = generatedText;
    }

    public long getProcessingTimeMs() {
        return processingTimeMs;
    }

    public void setProcessingTimeMs(long processingTimeMs) {
        this.processingTimeMs = processingTimeMs;
    }

    public Plan getCurrentPlan() {
        return currentPlan;
    }

    public void setCurrentPlan(Plan currentPlan) {
        this.currentPlan = currentPlan;
    }

    public int getConsumedTokens() {
        return consumedTokens;
    }

    public void setConsumedTokens(int consumedTokens) {
        this.consumedTokens = consumedTokens;
    }

    public int getPromptTokens() {
        return promptTokens;
    }

    public void setPromptTokens(int promptTokens) {
        this.promptTokens = promptTokens;
    }

    public int getOutputTokensEstimate() {
        return outputTokensEstimate;
    }

    public void setOutputTokensEstimate(int outputTokensEstimate) {
        this.outputTokensEstimate = outputTokensEstimate;
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

    public Integer getRequestsUsedInCurrentMinute() {
        return requestsUsedInCurrentMinute;
    }

    public void setRequestsUsedInCurrentMinute(Integer requestsUsedInCurrentMinute) {
        this.requestsUsedInCurrentMinute = requestsUsedInCurrentMinute;
    }

    public Integer getRequestsRemainingInCurrentMinute() {
        return requestsRemainingInCurrentMinute;
    }

    public void setRequestsRemainingInCurrentMinute(Integer requestsRemainingInCurrentMinute) {
        this.requestsRemainingInCurrentMinute = requestsRemainingInCurrentMinute;
    }
}
