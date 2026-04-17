package com.slop.gpt.model;

public class GenerationRequest {
    private String userId;
    private String prompt;
    private int maxOutputTokens;

    private int promptTokens;
    private int outputTokensEstimate;
    private int consumedTokens;

    private Integer requestsUsedInCurrentMinute;
    private Integer requestsRemainingInCurrentMinute;

    public GenerationRequest(String userId, String prompt, int maxOutputTokens) {
        this.userId = userId;
        this.prompt = prompt;
        this.maxOutputTokens = maxOutputTokens;
    }

    public String getUserId() {
        return userId;
    }

    public String getPrompt() {
        return prompt;
    }

    public int getMaxOutputTokens() {
        return maxOutputTokens;
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

    public int getConsumedTokens() {
        return consumedTokens;
    }

    public void setConsumedTokens(int consumedTokens) {
        this.consumedTokens = consumedTokens;
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
